package com.jedi.lightsabershop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jedi.jedishared.Component;
import com.jedi.jedishared.Image;
import com.jedi.jedishared.Item;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    ImageView imageView = findViewById(R.id.item_image);

    //item.setImageId(UUID.fromString("7f3d4054-deee-4179-81c6-aa317e46a84f")); //test id
    ImageApi imageApi = getRetrofit().create(ImageApi.class);
    if (item.getImageId() != null) {
      Call<Image> imageCall = imageApi.getImage(item.getImageId());
      imageCall.enqueue(new Callback<Image>() {
        @Override
        public void onResponse(Call<Image> call, Response<Image> response) {
          Image image = response.body();
          setImageFromByteArray(imageView, image.getImage());
        }

        @Override
        public void onFailure(Call<Image> call, Throwable t) {
          Log.e("ItemDetailsActivity", "Error fetching image: " + t.getMessage());
        }
      });
    }

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

  public static void setImageFromByteArray(ImageView imageView, byte[] byteArray) {
    if (byteArray != null && byteArray.length > 0) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
      imageView.setImageBitmap(bitmap);
    } else {
      // Handle the case where the byte array is null or empty (e.g., set a placeholder image)
      imageView.setImageResource(R.drawable.rounded_tost_background_fail);
    }
  }

  String imageString(){
    return "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";
  }
}