package com.prasad.techinterview.domain.repository

import com.prasad.techinterview.domain.model.Product


interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
}