package com.jedi.lightsabershop.api;

import com.jedi.jedishared.Item;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ItemApi {
  @POST("/lightsaberItem")
  Call<UUID> createItem(@Body Item item);
  
  @GET("/lightsaberItem")
  Call<List<Item>> getItems();
  
  @DELETE("/lightsaberItem/{id}")
  Call<Void> deleteItem(@Path("id") UUID id);

  @POST("/lightsaberItem/{id}")
  Call<Void> update(@Path("id") UUID id, @Body Item item);
  
}