package com.jedi.lightsabershop.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;
import com.jedi.lightsabershop.ItemDetailsActivity;
import com.jedi.lightsabershop.MainActivity;
import com.jedi.lightsabershop.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {
  private final List<Item> localDataSet;
  private List<Item> filteredList;
  private Context context;
  
  @Override
  public Filter getFilter() {
    
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        String filterString = constraint.toString().toLowerCase();
        FilterResults results = new FilterResults();
        
        if (filterString.isEmpty()) {
          results.values = localDataSet;
          results.count = localDataSet.size();
        } else {
          List<Item> filteredItems = new ArrayList<>();
          for (Item item : localDataSet) {
            if (item.getName().toLowerCase().contains(filterString)) {
              filteredItems.add(item);
            }
          }
          results.values = filteredItems;
          results.count = filteredItems.size();
        }
        
        return results;
      }
      
      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredList = (List<Item>) results.values;
        getSorted(filteredList);
        notifyDataSetChanged();
      }
    };
  }

  public void getSorted(List<Item> localDataSet) {
    localDataSet.sort(new Comparator<Item>() {
      @Override
      public int compare(Item item1, Item item2) {
        return componentTranslator(item1.getComponent()).compareTo(componentTranslator(item2.getComponent()));
      }
    });
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final TextView textViewType;
    private final TextView textViewPrice;
    private final ImageButton buttonAddToCart;
    
    public ViewHolder(View view) {
      super(view);
      textView = view.findViewById(R.id.rowItemTitle);
      textViewType = view.findViewById(R.id.rowItemType);
      textViewPrice = view.findViewById(R.id.rowItemPrice);
      buttonAddToCart = view.findViewById(R.id.buttonAddToCart);
    }
    
    public TextView getTextView() {
      return textView;
    }
    public TextView getTextViewType() {
      return textViewType;
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
    getSorted(localDataSet);
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
    Item currentItem;
    if (filteredList != null){
      currentItem = filteredList.get(position);
    }
    else {
      currentItem = localDataSet.get(position);
    }
    
    viewHolder.getTextView().setText(currentItem.getName());
    viewHolder.getTextViewType().setText(componentTranslator(currentItem.getComponent()));
    viewHolder.getTextViewPrice().setText("₡" + currentItem.getPrice()); // Assuming price is a float/double
    
    viewHolder.getButtonAddToCart().setOnClickListener(v -> {
      ((MainActivity) context).cart.addItem(currentItem);
      context.getString(R.string.added_to_cart);
      CustomToast(context, context.getString(R.string.added_to_cart) +": " + currentItem.getName(), true, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    });
    
    viewHolder.itemView.setOnClickListener(view -> {
      Intent intent = new Intent(context, ItemDetailsActivity.class);
      intent.putExtra("item", currentItem);
      context.startActivity(intent);
    });
  }
  
  private String componentTranslator(Component component) {
    if (context == null) {
      return "";
    }
    return switch (component) {
      case BLADE_EMITTER -> context.getString(R.string.blade_emitter);
      case FOCUSING_LENS -> context.getString(R.string.focusing_lens);
      case CYCLING_FIELD_ENERGIZERS -> context.getString(R.string.cycling_field_energizers);
      case MAIN_HILT -> context.getString(R.string.main_hilt);
      case KYBER_CRYSTAL -> context.getString(R.string.kyber_crystal);
      case LIGHTSABER_ENERGY_CORE -> context.getString(R.string.lightsaber_energy_core);
      case HAND_GRIP -> context.getString(R.string.hand_grip);
      case INERT_POWER_INSULATOR -> context.getString(R.string.inertia_power_insulator);
      case POMMEL_CAP -> context.getString(R.string.pomelle_cap);
    };
  }
  
  @Override
  public int getItemCount() {
    if (filteredList != null) {
      return filteredList.size();
    }
    return localDataSet.size();
  }
  
  public void CustomToast(Context context,String text, boolean success, int gravity, int duration)
  {
    Toast toast = new Toast(context.getApplicationContext());
    TextView textView=new TextView(context);
    textView.setText(text);
    if (success)
      textView.setBackgroundResource(R.drawable.rounded_tost_background_success);
    else
      textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
    textView.setTextColor(Color.WHITE);
    textView.setPadding(20, 20, 20, 20);
    toast.setDuration(duration);
    toast.setGravity(gravity, 0, 100);
    toast.setView(textView);
    toast.show();
  }
}
