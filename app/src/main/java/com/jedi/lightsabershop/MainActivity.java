package com.jedi.lightsabershop;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
  private ItemAdapter itemAdapter;
  
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
    
    SearchView searchView = findViewById(R.id.searchBar);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }
      
      @Override
      public boolean onQueryTextChange(String newText) {
        if (itemAdapter != null) {
          itemAdapter.getFilter().filter(newText);
        }
        return true;
      }
    });
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    ItemApi itemApi = getRetrofit().create(ItemApi.class);
    Call<List<Item>> savedItems = itemApi.getItems();
    
    List<Item> items = new ArrayList<>();
    savedItems.enqueue(new Callback<List<Item>>() {
      @Override
      public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
        if (response.isSuccessful() && response.body() != null) {
          items.clear(); // Clear existing items
          for (Item item : response.body()) {
            items.add(new Item(item.getId(), item.getName(), item.getComponent(), item.getPrice(), item.getDescription()));
          }
          runOnUiThread(() -> {
              RecyclerView recyclerView = findViewById(R.id.recyclerView);
              recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
              itemAdapter = new ItemAdapter(items, MainActivity.this);
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
    
    items.add(new Item(UUID.randomUUID(), "Simple Blade Emitter", Component.BLADE_EMITTER, 100.0, "Avery boring blade emitter"));
    items.add(new Item(UUID.randomUUID(), "Precision Focusing Lens", Component.FOCUSING_LENS, 75.50, "Enhances blade stability"));
    items.add(new Item(UUID.randomUUID(), "Dual-Phase Energizers", Component.CYCLING_FIELD_ENERGIZERS, 150.0, "Provides faster blade ignition"));
    items.add(new Item(UUID.randomUUID(), "Ergonomic Main Hilt", Component.MAIN_HILT, 200.0, "Comfortable grip for extended use"));
    items.add(new Item(UUID.randomUUID(), "Blue Kyber Crystal", Component.KYBER_CRYSTAL, 500.0, "Imbues the blade with blue energy"));
    items.add(new Item(UUID.randomUUID(), "High-Capacity Energy Core", Component.LIGHTSABER_ENERGY_CORE, 300.0, "Extends lightsaber power reserves"));
    items.add(new Item(UUID.randomUUID(), "Leather Hand Grip", Component.HAND_GRIP, 50.0, "Adds a classic touch"));
    items.add(new Item(UUID.randomUUID(), "Reinforced Power Insulator", Component.INERT_POWER_INSULATOR, 80.0, "Protects against electrical surges"));
    items.add(new Item(UUID.randomUUID(), "Ornate Pommel Cap", Component.POMMEL_CAP, 60.0, "A decorative finishing touch"));
    
    return items;
  }
}