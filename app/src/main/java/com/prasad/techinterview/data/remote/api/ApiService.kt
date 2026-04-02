package com.prasad.techinterview.data.remote.api

import com.prasad.techinterview.data.remote.dto.ProductResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponseDto>
}
