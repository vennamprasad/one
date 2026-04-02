package com.prasad.techinterview.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)

data class ProductResponseDto(
    @SerializedName("products") var products: List<ProductDto> = emptyList(),
)