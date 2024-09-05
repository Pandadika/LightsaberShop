package com.jedi.lightsabershop;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class CreateItemActivity extends AppCompatActivity {

    EditText itemNameInput, itemPriceInput, itemDescriptionInput;
    Spinner itemComponentSpinner;
    Button createItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        itemNameInput = findViewById(R.id.itemName);
        itemComponentSpinner = findViewById(R.id.itemComponent);
        itemPriceInput = findViewById(R.id.itemPrice);
        itemDescriptionInput = findViewById(R.id.itemDescription);
        createItemButton = findViewById(R.id.createItemButton);

        itemComponentSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Component.getFormattedNames() ));

        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemNameInput.getText().toString();
                Component component = (Component) itemComponentSpinner.getSelectedItem();
                double price = Double.parseDouble(itemPriceInput.getText().toString());
                String description = itemDescriptionInput.getText().toString();

                Item newItem = new Item(name, component, price, description);

                // Save the newItem to your database or perform other actions

                Toast.makeText(CreateItemActivity.this, "New component created!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}