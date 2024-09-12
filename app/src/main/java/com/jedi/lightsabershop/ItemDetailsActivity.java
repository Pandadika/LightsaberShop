package com.jedi.lightsabershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jedi.jedishared.Component;
import com.jedi.jedishared.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends BaseActivity {
  
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_details);
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setOnClickListener(view ->{
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    
    Intent intent = getIntent();
    Item item = (Item) intent.getSerializableExtra("item");
    
    TextView itemNameText = findViewById(R.id.item_name_text);
    TextView itemTypeText = findViewById(R.id.item_type_text);
    TextView itemPriceText = findViewById(R.id.item_price_text);
    TextView itemDescriptionText = findViewById(R.id.item_description_text);
    Button addToCartButton = findViewById(R.id.addToCartButton);
    Button editButton = findViewById(R.id.editButton);
    Button deleteButton = findViewById(R.id.deleteButton);
    editButton.setVisibility(View.GONE);
    deleteButton.setVisibility(View.GONE);
    
    if (item != null) {
      itemPriceText.setText(getString(R.string.price) + " ₡" + item.getPrice());
      itemTypeText.setText(getString(R.string.type) + " " + componentTranslator(item.getComponent()));
      itemNameText.setText(getString(R.string.item_name) + " " + item.getName());
      itemDescriptionText.setText(getString(R.string.description) + " " + item.getDescription());
    }
    
    addToCartButton.setOnClickListener(view -> {
      this.cart.addItem(item);
      CustomToast(ItemDetailsActivity.this, this.getString(R.string.added_to_cart) +": " + item.getName(), true, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    });
    
    DecodedJWT decodedJWT = tryDecodedJWT();
    if (decodedJWT != null) {
      Claim rolesClaim = decodedJWT.getClaim("roles");
      String roles = rolesClaim.asString();
      if (roles.contains("ADMIN")) {
        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
      }
    }

    editButton.setOnClickListener(view -> {
      Intent intent1 = new Intent(this, EditItemActivity.class);
      intent1.putExtra("item", item);
      startActivity(intent1);
    });
    
    deleteButton.setOnClickListener(view -> {
      ItemApi itemApi = getRetrofit().create(ItemApi.class);
      Call<Void> call = itemApi.deleteItem(item.getId());
      call.enqueue(new Callback<Void>() {
        
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
          if (response.isSuccessful()) {
            CustomToast(ItemDetailsActivity.this, ItemDetailsActivity.this.getString(R.string.item_delete_success), true, Gravity.TOP, Toast.LENGTH_SHORT);
            finish();
          }
        }
        
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
          CustomToast(ItemDetailsActivity.this, ItemDetailsActivity.this.getString(R.string.item_delete_failt), false, Gravity.TOP, Toast.LENGTH_SHORT);
        }
      });
    });
  }
  private String componentTranslator(Component component) {
    return switch (component) {
      case BLADE_EMITTER -> this.getString(R.string.blade_emitter);
      case FOCUSING_LENS -> this.getString(R.string.focusing_lens);
      case CYCLING_FIELD_ENERGIZERS -> this.getString(R.string.cycling_field_energizers);
      case MAIN_HILT -> this.getString(R.string.main_hilt);
      case KYBER_CRYSTAL -> this.getString(R.string.kyber_crystal);
      case LIGHTSABER_ENERGY_CORE -> this.getString(R.string.lightsaber_energy_core);
      case HAND_GRIP -> this.getString(R.string.hand_grip);
      case INERT_POWER_INSULATOR -> this.getString(R.string.inertia_power_insulator);
      case POMMEL_CAP -> this.getString(R.string.pomelle_cap);
    };
  }
  
}