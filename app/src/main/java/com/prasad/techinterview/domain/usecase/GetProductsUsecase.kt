package com.prasad.techinterview.domain.usecase

import com.prasad.techinterview.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke() = productRepository.getProducts()
}