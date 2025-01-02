package com.example.ocandroidp12.domain.model

data class Cloth(
    val id: Int,
    val picture: Picture,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    val original_price: Double
)

data class Picture(
    val url: String,
    val description: String
)


