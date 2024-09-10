package com.jedi.lightsabershop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jedi.jedishared.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
  
  private EditText editTextUsername;
  private EditText editTextPassword;
  private Button buttonLogin;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UserApi userApi = retrofit.create(UserApi.class);
    setContentView(R.layout.activity_login);
    
    editTextUsername = findViewById(R.id.editTextUsername);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    
    buttonLogin.setOnClickListener(view ->
    {
      User login = new User();
      login.setUsername(editTextUsername.getText().toString());
      login.setPassword(editTextPassword.getText().toString());
      
      TextView textView = new TextView(LoginActivity.this);
      textView.setText("No connection");
      textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
      Call<Boolean> call = userApi.login(login);
      call.enqueue(
          new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
              if (response.isSuccessful() && response.body() != null && response.body()){
                textView.setText("Success");
                textView.setBackgroundResource(R.drawable.rounded_tost_background_success);
              }
              else {
                textView.setText("Login failed");
              }
            }
            
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
              textView.setText("Login failed");
            }
          }
      );
      textView.setTextColor(Color.WHITE);
      textView.setPadding(20, 20, 20, 20);
      Toast toast = new Toast(LoginActivity.this);
      toast.setView(textView);
      toast.setDuration(Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.TOP, 0, 100);
      toast.show();
      
    });
  }
}