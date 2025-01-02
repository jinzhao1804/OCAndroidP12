package com.example.ocandroidp12.di

import com.example.ocandroidp12.data.ClothApiService
import com.example.ocandroidp12.data.RetrofitClient
import com.example.ocandroidp12.data.repository.ClotheRepository
import com.example.ocandroidp12.domain.usecase.GetAllClothesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideClothApiService(): ClothApiService {
        return RetrofitClient.api
    }

    @Provides
    fun provideClothesRepository(apiService: ClothApiService): ClotheRepository {
        return ClotheRepository(apiService)
    }

    @Provides
    fun provideGetAllClothesUseCase(clothesRepository: ClotheRepository): GetAllClothesUseCase {
        return GetAllClothesUseCase(clothesRepository)
    }
}
