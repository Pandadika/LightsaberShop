package com.jedi.lightsabershop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jedi.jedishared.Item;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
  private List<Item> localDataSet;
  private Context context;
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final TextView textViewPrice;
    private final ImageButton buttonAddToCart;
    
    public ViewHolder(View view) {
      super(view);
      textView = view.findViewById(R.id.rowItemTitle);
      textViewPrice = view.findViewById(R.id.rowItemPrice);
      buttonAddToCart = view.findViewById(R.id.buttonAddToCart);
    }
    
    public TextView getTextView() {
      return textView;
    }
    public TextView getTextViewPrice() {
      return textViewPrice;
    }
    public ImageButton getButtonAddToCart() {
      return buttonAddToCart;
    }
  }
  
  public ItemAdapter(List<Item> dataSet, Context context) {
    localDataSet = dataSet;
    this.context = context;
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.text_row_item, viewGroup, false);
    
    return new ViewHolder(view);
  }
  
/*  @Override
  public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
    viewHolder.getTextView().setText(localDataSet.get(position).getName());
    viewHolder.itemView.setOnClickListener(view -> {
      Item clickedItem = localDataSet.get(position);
      Intent intent = new Intent(context, ItemDetailsActivity.class);
      intent.putExtra("item", clickedItem);
      context.startActivity(intent);
    });
  }*/
  
  @Override
  public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
    Item currentItem = localDataSet.get(position);
    viewHolder.getTextView().setText(currentItem.getName());
    viewHolder.getTextViewPrice().setText("$" + currentItem.getPrice()); // Assuming price is a float/double
    
    viewHolder.getButtonAddToCart().setOnClickListener(v -> {
      ((MainActivity) context).cart.addItem(currentItem);
      Toast.makeText(context, "Added to cart: " + currentItem.getName(), Toast.LENGTH_SHORT).show();
    });
    
    viewHolder.itemView.setOnClickListener(view -> {
      Intent intent = new Intent(context, ItemDetailsActivity.class);
      intent.putExtra("item", currentItem);
      context.startActivity(intent);
    });
  }
  
  ;
  
  @Override
  public int getItemCount() {
    return localDataSet.size();
  }
}
