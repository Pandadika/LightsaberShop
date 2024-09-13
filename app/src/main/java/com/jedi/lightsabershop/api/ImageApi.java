package com.jedi.lightsabershop.api;

import com.jedi.jedishared.Image;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ImageApi {
  @GET("/image/{id}")
  Call<Image> getImage(@Path("id") UUID id);;

  @POST("/image/upload")
  Call<UUID> uploadImage(@Body Image image);
}
