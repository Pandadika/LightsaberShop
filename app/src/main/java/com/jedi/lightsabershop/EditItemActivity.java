package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;
import com.jedi.jedishared.User;

public class EditItemActivity extends BaseActivity {
  EditText itemName;
  EditText itemDescription;
  EditText itemPrice;
  Spinner itemComponent;
  Button updateItemButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_item);
    ItemApi itemApi = getRetrofit().create(ItemApi.class);
    
    itemName = findViewById(R.id.itemName);
    itemDescription = findViewById(R.id.itemDescription);
    itemPrice = findViewById(R.id.itemPrice);
    itemComponent = findViewById(R.id.itemComponent);
    updateItemButton = findViewById(R.id.updateItemButton);
    itemComponent.setAdapter(new ComponentSpinnerAdapter(this, Component.values()));
    
    Item item = (Item) this.getIntent().getSerializableExtra("item");
    itemName.setText(item.getName());
    itemDescription.setText(item.getDescription());
    itemPrice.setText(String.valueOf(item.getPrice()));
    itemComponent.setSelection(item.getComponent().ordinal());
    
    updateItemButton.setOnClickListener(v -> {
      Item updatedItem = new Item();
      updatedItem.setName(itemName.getText().toString());
      updatedItem.setDescription(itemDescription.getText().toString());
      updatedItem.setPrice(Double.parseDouble(itemPrice.getText().toString()));
      updatedItem.setComponent((Component) itemComponent.getSelectedItem());
      
      Call<Void> updateCall = itemApi.update(updatedItem.getId(), updatedItem);
      updateCall.enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
          if (response.isSuccessful()) {
            CustomToast(EditItemActivity.this, "Item updated successfully", true, Gravity.TOP, Toast.LENGTH_SHORT);
          }
        }
        
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
          CustomToast(EditItemActivity.this, "Failed to update item", false, Gravity.TOP, Toast.LENGTH_SHORT);
        }
      });
    });
  }
}
