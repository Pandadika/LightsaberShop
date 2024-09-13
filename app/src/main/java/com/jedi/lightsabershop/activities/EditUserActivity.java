package com.jedi.lightsabershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jedi.jedishared.User;
import com.jedi.lightsabershop.R;
import com.jedi.lightsabershop.api.UserApi;

public class EditUserActivity extends BaseActivity {
  
  TextView textViewUsername;
  EditText editTextFirstname, editTextSurname, editTextPassword, editTextEmail;
  Button buttonSave;
  Button buttonLogout;
  Button buttonAdminPage;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_user);
    UserApi userApi = getRetrofit().create(UserApi.class);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    textViewUsername = findViewById(R.id.textViewUsername);
    editTextFirstname = findViewById(R.id.editTextFirstname);
    editTextSurname = findViewById(R.id.editTextSurname);
    editTextPassword = findViewById(R.id.editTextPassword);
    editTextEmail = findViewById(R.id.editTextEmail);
    
    DecodedJWT decodedJWT = tryDecodedJWT();
    Call<User> call = userApi.user(decodedJWT.getSubject());
    call.enqueue(new Callback<User>() {
                   @Override
                   public void onResponse(Call<User> call, Response<User> response) {
                     if (response.isSuccessful()) {
                       User user = response.body();
                       textViewUsername.setText(user.getUsername());
                       editTextFirstname.setText(user.getFirstName());
                       editTextSurname.setText(user.getSurName());
                       editTextEmail.setText(user.getEmail());
                     } else {
                       CustomToast(EditUserActivity.this, "Failed to update user", false, Gravity.TOP, Toast.LENGTH_SHORT);
                     }
                   }
                   
                   @Override
                   public void onFailure(Call<User> call, Throwable t) {
                   
                   }
                 });
    
    buttonAdminPage = findViewById(R.id.adminButton);
    buttonAdminPage.setVisibility(View.GONE);
    buttonAdminPage.setEnabled(false);
    Claim rolesClaim = decodedJWT.getClaim("roles");
    String roles = rolesClaim.asString();
    if (roles.contains("ADMIN")) {
      buttonAdminPage.setEnabled(true);
      buttonAdminPage.setVisibility(View.VISIBLE);
      buttonAdminPage.setOnClickListener(v -> {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
      });
    }
    
    
    buttonLogout = findViewById(R.id.buttonLogout);
    buttonLogout.setOnClickListener(v -> {
      logout();
    });
    
    buttonSave = findViewById(R.id.buttonSave);
    buttonSave.setOnClickListener(v -> {
      String firstName = editTextFirstname.getText().toString();
      String lastName = editTextSurname.getText().toString();
      String password = editTextPassword.getText().toString();
      String email = editTextEmail.getText().toString();
      
      User newUser = new User();
      newUser.setUsername(decodedJWT.getSubject());
      newUser.setFirstName(firstName);
      newUser.setSurName(lastName);
      if (!password.isEmpty()) {
        newUser.setPassword(password);
      }
      newUser.setEmail(email);
      if (roles.contains("ADMIN")){
        newUser.setIsAdmin(true);
      }
      
      
      Call<User> updateCall = userApi.update(decodedJWT.getSubject(), newUser);
      updateCall.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
          User user = response.body();
          if (response.isSuccessful()) {
            editTextFirstname.setText(user.getFirstName());
            editTextSurname.setText(user.getSurName());
            editTextEmail.setText(user.getEmail());
            CustomToast(EditUserActivity.this, EditUserActivity.this.getString(R.string.user_updated_successfully), true, Gravity.TOP, Toast.LENGTH_SHORT);
          }
          else {
            CustomToast(EditUserActivity.this, EditUserActivity.this.getString(R.string.user_update_failed), false, Gravity.TOP, Toast.LENGTH_SHORT);
          }
        }
        
        @Override
        public void onFailure(Call<User> call, Throwable t) {
        
        }
      });
    });
    
  }
}