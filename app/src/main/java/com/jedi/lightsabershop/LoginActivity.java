package com.jedi.lightsabershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.jedi.jedishared.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
  
  private EditText editTextUsername;
  private EditText editTextPassword;
  private Button buttonLogin;
  private Button buttonRegister;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UserApi userApi = getRetrofit().create(UserApi.class);
    setContentView(R.layout.activity_login);
    
    editTextUsername = findViewById(R.id.editTextUsername);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    buttonRegister = findViewById(R.id.createUserButton);
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setOnClickListener(view ->{
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    
    buttonLogin.setOnClickListener(view ->
    {
      User login = new User();
      login.setUsername(editTextUsername.getText().toString());
      login.setPassword(editTextPassword.getText().toString());
      
      final Boolean[] isConnected = {false};
      
      Call<JwtResponse> call = userApi.login(login);
      call.enqueue(
          new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
              if (response.isSuccessful() && response.body() != null){
                isConnected[0] = true;
                CustomToast(LoginActivity.this, "Login successful!", true, Gravity.TOP, Toast.LENGTH_SHORT);
                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("jwt_token", response.body().getToken());
                editor.apply();
/*                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
              }
              else {
                CustomToast(LoginActivity.this, "Login failed", false, Gravity.TOP, Toast.LENGTH_SHORT);
              }
            }
            
            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
              CustomToast(LoginActivity.this, "Login failed", false, Gravity.TOP, Toast.LENGTH_SHORT);
            }
          }
      );
      if(!isConnected[0]){
        CustomToast(LoginActivity.this, "No connection", false, Gravity.TOP, Toast.LENGTH_SHORT);
      }
    });
    
    buttonRegister.setOnClickListener(view -> {
      Intent intent = new Intent(this, RegisterActivity.class);
      startActivity(intent);
    });
  }
}