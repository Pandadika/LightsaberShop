package com.jedi.lightsabershop;

import com.jedi.jedishared.Item;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ItemApi {
  @POST("/lightsaberItem")
  Call<UUID> createItem(@Body Item item);
  
  @GET("/lightsaberItem")
  Call<List<Item>> getItems();

}