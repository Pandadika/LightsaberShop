package com.jedi.lightsabershop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public abstract class BaseActivity extends AppCompatActivity {
  String ip;
  int port;
  String apiUrl;
  
  Cart cart = CartSingleton.getInstance().getCart();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    SharedPreferences sharedPreferences = getSharedPreferences("network_settings", MODE_PRIVATE);
    ip = sharedPreferences.getString("ip", "192.168.1.233"); // Default IP
    port = sharedPreferences.getInt("port", 8080); // Default port
    apiUrl = "http://" + ip + ":" + port + "/";
    
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
  
  public void updateNetworkSettings(String ipAddress, int port) {
    this.ip = ipAddress;
    this.port = port;
    this.apiUrl = "http://" + this.ip + ":" + this.port + "/";
    SharedPreferences sharedPreferences = getSharedPreferences("network_settings", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("ip", this.ip);
    editor.putInt("port", this.port);
    editor.apply();
  }
  
  public Retrofit getRetrofit() {
    return new Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
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
    } else if (id == R.id.login) {
      DecodedJWT decodedJWT = tryDecodedJWT();
      Intent intent;
      if (decodedJWT != null) {
        Claim rolesClaim = decodedJWT.getClaim("roles");
        String roles = rolesClaim.asString();
        if (roles.contains("admin")) {
          intent = new Intent(this, AdminActivity.class);
          startActivity(intent);
        } else {
          intent = new Intent(this, EditUserActivity.class); //TODO make this the user page
          startActivity(intent);
        }
      }
      else {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
      }
      return true;
    } else if (id == R.id.cart) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
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
  
  protected @NonNull SharedPreferences getSharedPreferences() {
    SharedPreferences sharedPreferences = null;
    try {
      sharedPreferences = androidx.security.crypto.EncryptedSharedPreferences.create(
          "encrypted_prefs",
          androidx.security.crypto.MasterKeys.getOrCreate(androidx.security.crypto.MasterKeys.AES256_GCM_SPEC),
          getApplicationContext(),
          androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
          androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      );
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return sharedPreferences;
  }
  
  protected DecodedJWT tryDecodedJWT() {
    SharedPreferences sharedPreferences = getSharedPreferences();
    String jwt = sharedPreferences.getString("jwt_token", null);
    if (jwt != null) {
      return JWT.decode(jwt);
    } else {
      return null;
    }
  }
  
  protected boolean isLoggedIn() {
    return tryDecodedJWT() != null;
  }

  protected void logout() {
    SharedPreferences sharedPreferences = getSharedPreferences();
    sharedPreferences.edit().remove("jwt_token").apply();
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
    startActivity(intent);
  }
  
  public void CustomToast(Context context,String text, boolean success, int gravity, int duration)
  {
    Toast toast = new Toast(context.getApplicationContext());
    TextView textView=new TextView(context);
    textView.setText(text);
    if (success)
      textView.setBackgroundResource(R.drawable.rounded_tost_background_success);
    else
      textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
    textView.setTextColor(Color.WHITE);
    textView.setPadding(20, 20, 20, 20);
    toast.setDuration(duration);
    toast.setGravity(gravity, 0, 100);
    toast.setView(textView);
    toast.show();
  }
}