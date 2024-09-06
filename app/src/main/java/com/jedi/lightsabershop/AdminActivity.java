package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AdminActivity extends BaseActivity {
  
  Button createItemButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);
    
    createItemButton = findViewById(R.id.createItemButton);
    createItemButton.setOnClickListener(view -> {
      Intent intent = new Intent(AdminActivity.this, CreateItemActivity.class);
      startActivity(intent);
    });
  }
}