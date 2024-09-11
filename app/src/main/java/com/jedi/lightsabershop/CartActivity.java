package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends BaseActivity implements CartAdapter.CartUpdateListener {
  
  private RecyclerView cartRecyclerView;
  private TextView textViewTotalItems;
  private TextView textViewTotalPrice;
  private Button buttonCheckout;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    
    cartRecyclerView = findViewById(R.id.cartRecyclerView);
    textViewTotalItems = findViewById(R.id.textViewTotalItems);
    textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
    buttonCheckout = findViewById(R.id.checkoutButton);
    
    CartAdapter cartAdapter = new CartAdapter(cart.getItems(), this);
    cartRecyclerView.setAdapter(cartAdapter);
    cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setOnClickListener(view ->{
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    
    updateCartSummary();
    
    buttonCheckout.setOnClickListener(view -> {
      if (cart.anyItems()){
        cart.emptyCart();
        cartAdapter.notifyDataSetChanged();
        updateCartSummary();
        CustomToast(this, "Checkout successful!", true, Gravity.TOP, Toast.LENGTH_SHORT);
      }
      else {
        CustomToast(this, "Cart is empty!", false, Gravity.TOP, Toast.LENGTH_SHORT);
      }
    });
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
    textViewTotalPrice.setText("Total Price: ₡ " + String.format("%.2f", cart.getTotalPrice()));
  }
}
