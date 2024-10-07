package com.example.krishna

import com.example.krishna.model.Data
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("getItembyShopId")
    @FormUrlEncoded
    suspend fun getItemByShopId(@Field("shop_id") shopId:String): Data
}