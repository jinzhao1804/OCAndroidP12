package com.example.ocandroidp12.domain.usecase

import com.example.ocandroidp12.data.repository.ClotheRepository
import com.example.ocandroidp12.domain.model.Cloth

class GetAllClothesUseCase(private val clotheRepository: ClotheRepository) {

    suspend fun execute(): List<Cloth> {
        return clotheRepository.getClothes()
    }
}
