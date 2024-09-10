package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ItemDetailsActivity extends BaseActivity {
  
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_details);
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    Intent intent = getIntent();
    Item item = (Item) intent.getSerializableExtra("item");
    
    TextView itemNameText = findViewById(R.id.item_name_text);
    TextView itemPriceText = findViewById(R.id.item_price_text);
    TextView itemDescriptionText = findViewById(R.id.item_description_text);
    
    if (item != null) {
      itemPriceText.setText(getString(R.string.price) + " " + item.price);
      itemNameText.setText(getString(R.string.item_name) + " " + item.name);
      itemDescriptionText.setText(getString(R.string.description) + " " + item.description);
    }
  }
}