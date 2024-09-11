package com.jedi.lightsabershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jedi.jedishared.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_user);
    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setOnClickListener(view ->{
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    
    EditText editTextUsername = findViewById(R.id.editTextUsername);
    EditText editTextFirstname = findViewById(R.id.editTextFirstname);
    EditText editTextSurname = findViewById(R.id.editTextSurname);
    EditText editTextEmail = findViewById(R.id.editTextEmail);
    EditText editTextPassword = findViewById(R.id.editTextPassword);
    EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
    Button buttonRegister = findViewById(R.id.buttonRegister);
    
    editTextUsername.addTextChangedListener(spaceChecker());
    editTextEmail.addTextChangedListener(spaceChecker());
    editTextPassword.addTextChangedListener(spaceChecker());
    editTextConfirmPassword.addTextChangedListener(spaceChecker());
    
    buttonRegister.setOnClickListener(view -> {
      String username = editTextUsername.getText().toString();
      String firstname = editTextFirstname.getText().toString();
      String surname = editTextSurname.getText().toString();
      String email = editTextEmail.getText().toString();
      String password = editTextPassword.getText().toString();
      String confirmPassword = editTextConfirmPassword.getText().toString();
      
      boolean isValid = true;
      if (username.isEmpty() ||
          firstname.isEmpty() ||
          surname.isEmpty() ||
          email.isEmpty() ||
          password.isEmpty() ||
          confirmPassword.isEmpty()) {
        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        isValid = false;
      } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
        isValid = false;
      } else if (!password.equals(confirmPassword)) {
        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        isValid = false;
      }
      
      
      if (isValid) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstname.trim());
        user.setSurName(surname.trim());
        user.setEmail(email);
        user.setPassword(password);
        
        registerUser(user);
      }
    });
  }
  
  private void registerUser(User user) {
    TextView textView = new TextView(RegisterActivity.this);
    textView.setText("No connection");
    textView.setBackgroundResource(R.drawable.rounded_tost_background_fail);
    textView.setTextColor(Color.WHITE);
    textView.setPadding(20, 1, 20, 1);
    
    UserApi userApi = getRetrofit().create(UserApi.class);
    Call<Boolean> call = userApi.register(user);
    call.enqueue(
        new Callback<Boolean>() {
          @Override
          public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            if (response.isSuccessful() && response.body() != null){
              textView.setText("User created");
              textView.setBackgroundResource(R.drawable.rounded_tost_background_success);
              showToast(textView);
              finish();
            }
            else {
              textView.setText("User creation failed");
              showToast(textView);
            }
          }
          
          @Override
          public void onFailure(Call<Boolean> call, Throwable t) {
            textView.setText("User creation failed");
            showToast(textView);
          }
        }
    );
    if(textView.getText() == "No connection"){
      showToast(textView);
    }
  }
  
  private TextWatcher spaceChecker() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }
      
      @Override
      public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (text.contains(" ")) {
          s.replace(0, s.length(), text.replaceAll(" ", ""));
        }
      }
    };
  }
  
  private void showToast(TextView textView) {
    Toast toast = new Toast(RegisterActivity.this);
    toast.setView(textView);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.TOP, 0, 100);
    toast.show();
  }
}