package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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
import com.jedi.lightsabershop.adapters.ItemAdapter;

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

//    Button button = findViewById(R.id.butt);
//    button.setOnClickListener(v -> {
//      Intent intent = new Intent(this, CameraActivity.class);
//      startActivity(intent);
//    });

    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    ItemApi itemApi = getRetrofit().create(ItemApi.class);
    Call<List<Item>> savedItems = itemApi.getItems();
    List<Item> items = new ArrayList<>();
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    itemAdapter = new ItemAdapter(items, MainActivity.this);
    recyclerView.setAdapter(itemAdapter);
    savedItems.enqueue(new Callback<List<Item>>() {
      @Override
      public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
        if (response.isSuccessful() && response.body() != null) {
          items.clear(); // Clear existing items
          for (Item item : response.body()) {
            items.add(new Item(item.getId(), item.getName(), item.getComponent(), item.getPrice(), item.getDescription(), item.getImageId()));
          }
          runOnUiThread(() -> {
            itemAdapter.getSorted(items); // Sort the updated list
            itemAdapter.notifyDataSetChanged();
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
}