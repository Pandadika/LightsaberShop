package com.jedi.lightsabershop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jedi.jedishared.Component;
import com.jedi.jedishared.Image;
import com.jedi.jedishared.Item;
import com.jedi.lightsabershop.adapters.ComponentSpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateItemActivity extends BaseActivity {
  
  EditText itemNameInput, itemPriceInput, itemDescriptionInput;
  Spinner itemComponentSpinner;
  Button createItemButton;
  private ActivityResultLauncher<Intent> takePictureLauncher;
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private byte[] capturedImageByteArray;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ItemApi itemApi = getRetrofit().create(ItemApi.class);
    ImageApi imageApi = getRetrofit().create(ImageApi.class);
    
    setContentView(R.layout.activity_create_item);
    
    itemNameInput = findViewById(R.id.itemName);
    itemComponentSpinner = findViewById(R.id.itemComponent);
    itemPriceInput = findViewById(R.id.itemPrice);
    itemDescriptionInput = findViewById(R.id.itemDescription);
    createItemButton = findViewById(R.id.createItemButton);
    Button takePictureButton = findViewById(R.id.takePictureButton);

    itemComponentSpinner.setAdapter(new ComponentSpinnerAdapter(this, Component.values()));

    takePictureLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
              Uri imageUri = data.getData();
              if (imageUri != null) {
                try {
                  Bitmap originalBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                  if (originalBitmap != null) {
                    // Compress the bitmap if needed
                    Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, 640, 480, true);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    capturedImageByteArray = byteArrayOutputStream.toByteArray();

                    originalBitmap.recycle();
                    compressedBitmap.recycle();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        });

          takePictureButton.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(CreateItemActivity.this, CameraActivity.class);
            takePictureLauncher.launch(takePictureIntent);
          });

          createItemButton.setOnClickListener(view -> {
            String name = itemNameInput.getText().toString();
            Component component = (Component) itemComponentSpinner.getSelectedItem();
            double price = Double.parseDouble(itemPriceInput.getText().toString());
            String description = itemDescriptionInput.getText().toString();

            Item newItem = new Item(UUID.randomUUID(), name, component, price, description, null); //UUID should realy be handled by db
            Gson gson = new Gson();

            if (capturedImageByteArray != null) {
              Image newImage = new Image();
              newImage.setId(UUID.randomUUID());
              newImage.setImage(capturedImageByteArray);
              newItem.setImageId(newImage.getId());

              Call<UUID> uploadedImagecall = imageApi.uploadImage(newImage);
              uploadedImagecall.enqueue(new Callback<UUID>() {

                @Override
                public void onResponse(Call<UUID> call, Response<UUID> response) {

                }

                @Override
                public void onFailure(Call<UUID> call, Throwable t) {

                }
              });
            }

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