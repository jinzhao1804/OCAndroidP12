package com.example.ocandroidp12.data.repository


import com.example.ocandroidp12.data.ClothApiService
import com.example.ocandroidp12.data.RetrofitClient
import com.example.ocandroidp12.domain.model.Cloth

class ClotheRepository(private val apiService: ClothApiService) {


    suspend fun getClothes(): List<Cloth> {
        return apiService.getClothes()
    }
}
