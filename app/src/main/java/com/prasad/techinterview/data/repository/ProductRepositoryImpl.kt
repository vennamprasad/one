package com.prasad.techinterview.data.repository

import com.prasad.techinterview.data.remote.api.ApiService
import com.prasad.techinterview.domain.model.Product
import com.prasad.techinterview.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let {
                    val res = it.products.map { dto ->
                        Product(
                            id = dto.id,
                            title = dto.title,
                            description = dto.description,
                        )
                    }
                    Result.success(res)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}