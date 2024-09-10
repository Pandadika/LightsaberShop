package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
  
  Button adminButton;
  Button loginButton;
  Button cartButton;
  
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
    
    loginButton = findViewById(R.id.loginpage);
    loginButton.setOnClickListener(view -> {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
    });
    
    cartButton = findViewById(R.id.cartpage);
    cartButton.setOnClickListener(view -> {
      Intent intent = new Intent(MainActivity.this, CartActivity.class);
      startActivity(intent);
    });
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    ItemApi itemApi = retrofit.create(ItemApi.class);
    Call<List<Item>> savedItems = itemApi.getItems();
    
    List<Item> items = new ArrayList<>();
    savedItems.enqueue(new Callback<List<Item>>() {
      @Override
      public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
        if (response.isSuccessful() && response.body() != null) {
          items.clear(); // Clear existing items
          for (Item item : response.body()) {
            items.add(new Item(item.getName(), item.getComponent(), item.getPrice(), item.getDescription()));
          }
          runOnUiThread(() -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            RecyclerView.Adapter<ItemAdapter.ViewHolder> itemAdapter = new ItemAdapter(items, MainActivity.this);
            recyclerView.setAdapter(itemAdapter);
          });
        } else {
          Log.e("MainActivity", "Error fetching items: API Response: " + response.message());
        }
      }
      
      @Override
      public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
        Log.e("MainActivity", "Error fetching items: Throwable ", t);
      }
    });
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