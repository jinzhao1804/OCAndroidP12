package com.example.ocandroidp12.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ClothApiService = retrofit.create(ClothApiService::class.java)
}
