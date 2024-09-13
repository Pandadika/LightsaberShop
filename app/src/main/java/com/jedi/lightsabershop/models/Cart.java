package com.jedi.lightsabershop.models;

import com.jedi.jedishared.Item;

import java.util.ArrayList;
import java.util.List;

public class Cart {
  
  private List<CartItem> items;
  
  public Cart() {
    items = new ArrayList<>();
  }
  
  public void addItem(Item item) {
    // Check if item already exists in cart
    for (CartItem cartItem : items) {
      if (cartItem.getItem().getId() == item.getId()) {
        // Item exists, increase quantity
        cartItem.incrementQuantity();
        return;
      }
    }
    // Item doesn't exist, add new CartItem
    items.add(new CartItem(item));
  }
  
  public void removeItem(Item item) {
    for (CartItem cartItem : items) {
      if (cartItem.getItem().getId() == item.getId()) {
        items.remove(cartItem);
        return;
      }
    }
  }
  
  public boolean anyItems() {
    return !items.isEmpty();
  }
  
  public List<CartItem> getItems() {
    return items;
  }
  
  public int getTotalItems() {
    int total = 0;
    for (CartItem cartItem : items) {
      total += cartItem.getQuantity();
    }
    return total;
  }
  
  public double getTotalPrice() {
    double total = 0;
    for (CartItem cartItem : items) {
      total += cartItem.getItem().getPrice() * cartItem.getQuantity();
    }
    return total;
  }
  
  public void emptyCart() {
    items.clear();
  }
  
  // Inner class for CartItem
  public static class CartItem {
    private Item item;
    private int quantity;
    
    public CartItem(Item item) {
      this.item = item;
      this.quantity = 1;
    }
    
    public Item getItem() {
      return item;
    }
    
    public int getQuantity() {
      return quantity;
    }
    
    public void incrementQuantity() {
      quantity++;
    }
    
    public void decrementQuantity() {
      if (quantity > 1) {
        quantity--;
      }
    }
  }
}