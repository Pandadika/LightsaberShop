package com.jedi.lightsabershop;

public class CartSingleton {
  private static CartSingleton instance;
  private Cart cart;
  
  private CartSingleton() {
    cart = new Cart();
  }
  
  public static CartSingleton getInstance() {
    if (instance == null) {
      instance = new CartSingleton();
    }
    return instance;
  }
  
  public Cart getCart() {
    return cart;
  }
}