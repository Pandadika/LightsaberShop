package com.jedi.lightsabershop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
  
  private List<Cart.CartItem> cartItems;
  private Context context;
  private CartUpdateListener cartUpdateListener;
  
  public CartAdapter(List<Cart.CartItem> cartItems, Context context) {
    this.cartItems = cartItems;
    this.context = context;
    this.cartUpdateListener = (CartUpdateListener) context;
  }
  
  public interface CartUpdateListener {
    void onCartUpdated();
  }
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewItemName;
    private final TextView textViewItemPrice;
    private final TextView textViewQuantity;
    private final ImageButton buttonRemove;
    
    public ViewHolder(View view) {
      super(view);
      textViewItemName = view.findViewById(R.id.textViewItemName);
      textViewItemPrice = view.findViewById(R.id.textViewItemPrice);
      textViewQuantity = view.findViewById(R.id.textViewQuantity);
      buttonRemove = view.findViewById(R.id.imageButtonRemove);
    }
    
    public ImageButton getButtonRemove() { return buttonRemove; }
  }
  
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_item, parent, false);
    return new ViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Cart.CartItem cartItem = cartItems.get(position);
    holder.textViewItemName.setText(cartItem.getItem().getName());
    holder.textViewItemPrice.setText("$" + String.format("%.2f", cartItem.getItem().getPrice()));
    holder.textViewQuantity.setText("Quantity: " + cartItem.getQuantity());
    
    holder.getButtonRemove().setOnClickListener(v -> {
      ((BaseActivity) context).cart.removeItem(cartItem.getItem());
      cartItems.remove(cartItem);
      notifyDataSetChanged();
      cartUpdateListener.onCartUpdated();
    });
  }
  
  @Override
  public int getItemCount() {
    return cartItems.size();
  }
  

}