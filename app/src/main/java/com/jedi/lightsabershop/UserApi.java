package com.jedi.lightsabershop;

import com.jedi.jedishared.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
  @POST("/user/login")
  Call<Boolean> login(@Body User user);
}
