package com.jedi.lightsabershop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends BaseActivity implements CartAdapter.CartUpdateListener {
  
  private RecyclerView cartRecyclerView;
  private TextView textViewTotalItems;
  private TextView textViewTotalPrice;
  //private Cart cart; // Your Cart object
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    
    cartRecyclerView = findViewById(R.id.cartRecyclerView);
    textViewTotalItems = findViewById(R.id.textViewTotalItems);
    textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
    
    // Get cart data (e.g., from Intent or Singleton)
    //cart = getCart(); // Replace with your cart retrieval logic
    
    CartAdapter cartAdapter = new CartAdapter(cart.getItems(), this);
    cartRecyclerView.setAdapter(cartAdapter);
    cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    
    updateCartSummary();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    updateCartSummary();
  }
  
  @Override
  public void onCartUpdated() {
    updateCartSummary();
  }
  
  private void updateCartSummary() {
    textViewTotalItems.setText("Total Items: " + cart.getTotalItems());
    textViewTotalPrice.setText("Total Price: $" + String.format("%.2f", cart.getTotalPrice()));
  }
  
/*  private Cart getCart() {
    return cart;
  }*/
}