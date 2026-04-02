package com.prasad.techinterview.domain.usecase

import com.prasad.techinterview.domain.model.Product
import com.prasad.techinterview.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetProductsUseCaseTest {

    private val productRepository: ProductRepository = mockk()
    private val getProductsUseCase = GetProductsUseCase(productRepository)

    @Test
    fun `invoke should return success result with product list when repository returns success`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, title = "Prod 1", description = "Desc 1"),
            Product(id = 2, title = "Prod 2", description = "Desc 2")
        )
        coEvery { productRepository.getProducts() } returns Result.success(products)

        // When
        val result = getProductsUseCase.invoke()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(products, result.getOrNull())
    }

    @Test
    fun `invoke should return failure result when repository returns error`() = runTest {
        // Given
        val exception = Exception("Network Error")
        coEvery { productRepository.getProducts() } returns Result.failure(exception)

        // When
        val result = getProductsUseCase.invoke()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
