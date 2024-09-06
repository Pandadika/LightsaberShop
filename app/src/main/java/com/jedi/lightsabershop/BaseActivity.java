package com.jedi.lightsabershop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
    String languageCode = prefs.getString("language", "en");
    setLocaleWithoutRecreate(languageCode);
    setContentView(R.layout.activity_main);
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    LocalBroadcastManager.getInstance(this).registerReceiver(localeChangeReceiver, new IntentFilter("LOCALE_CHANGED"));
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_language, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.english) {
      setLocale("en");
      return true;
    } else if (id == R.id.danish) {
      setLocale("dk");
      return true;
    } else if (id == R.id.huttese) {
      setLocale("nh");
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  private void setLocaleWithoutRecreate(String languageCode) {
    Locale locale = new Locale(languageCode);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.setLocale(locale);
    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
  }
  
  protected void setLocale(String languageCode) {
    Locale locale = new Locale(languageCode);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.setLocale(locale);
    //getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    
    SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
    prefs.edit().putString("language", languageCode).apply();
    
    Intent intent = new Intent("LOCALE_CHANGED");
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }
  
  private BroadcastReceiver localeChangeReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals("LOCALE_CHANGED")) {
        recreate();
      }
    }
  };
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(localeChangeReceiver);
  }
}