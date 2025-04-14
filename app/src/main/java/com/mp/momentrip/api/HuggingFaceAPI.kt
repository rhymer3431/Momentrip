package com.mp.momentrip.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HuggingFaceApi {
    @POST("models/skt/kobert-base-v1")
    fun compareTexts(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Call<JsonObject>
}