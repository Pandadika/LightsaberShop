package com.jedi.lightsabershop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.jedi.lightsabershop.R;

public class AdminActivity extends BaseActivity {
  
  Button createItemButton;
  EditText ipAddressEditText;
  EditText portEditText;
  Button logoutButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    ipAddressEditText = findViewById(R.id.ipAddressEditText);
    portEditText = findViewById(R.id.portEditText);
    createItemButton = findViewById(R.id.createItemButton);
    logoutButton = findViewById(R.id.logoutButton);
    
    SharedPreferences sharedPreferences = getSharedPreferences("network_settings", MODE_PRIVATE);
    ip = sharedPreferences.getString("ip", ip);
    port = sharedPreferences.getInt("port", port);
    
    ipAddressEditText.setText(ip);
    portEditText.setText(String.valueOf(port));
    
    Button saveButton = findViewById(R.id.updateNetworkSettingsButton);
    saveButton.setOnClickListener(view -> {
      updateNetworkSettings(ipAddressEditText.getText().toString(), Integer.parseInt(portEditText.getText().toString()));
      CustomToast(AdminActivity.this, "Network settings updated", true, Gravity.CENTER, Toast.LENGTH_SHORT);

      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString("ip", ip);
      editor.putInt("port", port);
      editor.apply();
      
      ipAddressEditText.setText(ip);
      portEditText.setText(String.valueOf(port));
    });
    
    createItemButton.setOnClickListener(view -> {
      Intent intent = new Intent(AdminActivity.this, CreateItemActivity.class);
      startActivity(intent);
    });
    
    logoutButton.setOnClickListener(view -> {
      logout();
    });
  }
}