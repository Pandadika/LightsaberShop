package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
  
  Button adminButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    adminButton = findViewById(R.id.adminpage);
    adminButton.setOnClickListener(view -> {
      Intent intent = new Intent(MainActivity.this, AdminActivity.class);
      startActivity(intent);
    });
    
    List<Item> items = new ArrayList<>(generateItems());
    
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    RecyclerView.Adapter<ItemAdapter.ViewHolder> ItemAdapter = new ItemAdapter(items, this);
    recyclerView.setAdapter(ItemAdapter);
  }
  
  public static List<Item> generateItems() {
    List<Item> items = new ArrayList<>();
    
    items.add(new Item("Simple Blade Emitter", Component.BLADE_EMITTER, 100.0, "Avery boring blade emitter"));
    items.add(new Item("Precision Focusing Lens", Component.FOCUSING_LENS, 75.50, "Enhances blade stability"));
    items.add(new Item("Dual-Phase Energizers", Component.CYCLING_FIELD_ENERGIZERS, 150.0, "Provides faster blade ignition"));
    items.add(new Item("Ergonomic Main Hilt", Component.MAIN_HILT, 200.0, "Comfortable grip for extended use"));
    items.add(new Item("Blue Kyber Crystal", Component.KYBER_CRYSTAL, 500.0, "Imbues the blade with blue energy"));
    items.add(new Item("High-Capacity Energy Core", Component.LIGHTSABER_ENERGY_CORE, 300.0, "Extends lightsaber power reserves"));
    items.add(new Item("Leather Hand Grip", Component.HAND_GRIP, 50.0, "Adds a classic touch"));
    items.add(new Item("Reinforced Power Insulator", Component.INERT_POWER_INSULATOR, 80.0, "Protects against electrical surges"));
    items.add(new Item("Ornate Pommel Cap", Component.POMMEL_CAP, 60.0, "A decorative finishing touch"));
    
    return items;
  }
}