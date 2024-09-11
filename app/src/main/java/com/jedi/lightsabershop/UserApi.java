package com.jedi.lightsabershop;

import com.jedi.jedishared.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
  @POST("/user/login")
  Call<JwtResponse> login(@Body User user);
  
  @POST("/user/create")
  Call<Boolean> register(@Body User user);
  
  @GET("/user/{username}")
  Call<User> user(@Path("username") String username);
  
  @POST("/user/update/{username}")
  Call<User> update(@Path("username") String username, @Body User user);
}

