package com.example.ocandroidp12.data

import com.example.ocandroidp12.domain.model.Cloth
import retrofit2.http.GET

interface ClothApiService {
    @GET("api/clothes.json")
    suspend fun getClothes(): List<Cloth>

}
