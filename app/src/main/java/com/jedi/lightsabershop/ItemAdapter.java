package com.jedi.lightsabershop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
  private List<Item> localDataSet;
  private Context context;
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    
    public ViewHolder(View view) {
      super(view);
      textView = view.findViewById(R.id.textView);
    }
    
    public TextView getTextView() {
      return textView;
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
  
  @Override
  public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
    viewHolder.getTextView().setText(localDataSet.get(position).name);
    viewHolder.itemView.setOnClickListener(view -> {
      Item clickedItem = localDataSet.get(position);
      Intent intent = new Intent(context, ItemDetailsActivity.class);
      intent.putExtra("item", clickedItem);
      context.startActivity(intent);
    });
  }
  
  ;
  
  @Override
  public int getItemCount() {
    return localDataSet.size();
  }
}
