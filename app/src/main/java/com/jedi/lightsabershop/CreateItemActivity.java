package com.jedi.lightsabershop;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateItemActivity extends BaseActivity {
  
  EditText itemNameInput, itemPriceInput, itemDescriptionInput;
  Spinner itemComponentSpinner;
  Button createItemButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ItemApi itemApi = retrofit.create(ItemApi.class);
    
    setContentView(R.layout.activity_create_item);
    
    itemNameInput = findViewById(R.id.itemName);
    itemComponentSpinner = findViewById(R.id.itemComponent);
    itemPriceInput = findViewById(R.id.itemPrice);
    itemDescriptionInput = findViewById(R.id.itemDescription);
    createItemButton = findViewById(R.id.createItemButton);
    
    itemComponentSpinner.setAdapter(new ComponentSpinnerAdapter(this, Component.values()));
    
    createItemButton.setOnClickListener(view -> {
      String name = itemNameInput.getText().toString();
      Component component = (Component) itemComponentSpinner.getSelectedItem();
      double price = Double.parseDouble(itemPriceInput.getText().toString());
      String description = itemDescriptionInput.getText().toString();
      
      Item newItem = new Item(name, component, price, description); //TODO Save this :D
      Gson gson = new Gson();
      String json = gson.toJson(newItem);
      
      TextView textView = new TextView(CreateItemActivity.this);
      textView.setText("No connection");
      textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
      Call<UUID> call = itemApi.createItem(newItem);
          call.enqueue(new Callback<UUID>() {
            @Override
            public void onResponse(@NonNull Call<UUID> call, @NonNull Response<UUID> response) {
              if (!response.isSuccessful()) {
                textView.setText(newItem.getName() + response.code());
                textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
                return;
              }
              textView.setText(newItem.getName() + " saved!");
              textView.setBackgroundResource(R.drawable.rounded_tost_background_success);
            }
            
            @Override
            public void onFailure(Call<UUID> call, Throwable t) {
              textView.setText(newItem.getName() + " failed!");
              textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
            }
          });

      textView.setTextColor(Color.WHITE);
      textView.setPadding(20, 20, 20, 20);
      
      Toast toast = new Toast(CreateItemActivity.this);
      toast.setView(textView);
      toast.setDuration(Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.TOP, 0, 100);
      toast.show();
      
      finish();
    });
  }
}
