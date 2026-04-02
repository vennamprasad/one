package com.prasad.techinterview.presentation.screen.products

import app.cash.turbine.test
import com.prasad.techinterview.domain.model.Product
import com.prasad.techinterview.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductScreenViewmodelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var viewModel: ProductScreenViewmodel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProductsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initially uiState should have isLoading=false and empty products`() = runTest {
        coEvery { getProductsUseCase.invoke() } returns Result.success(emptyList())

        viewModel = ProductScreenViewmodel(getProductsUseCase)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)
            assertTrue(initialState.products.isEmpty())
            
            // Advance to see loading
            this@runTest.runCurrent()
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Next is final state
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getproducts success should update uiState with product list`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, title = "Prod 1", description = "Desc 1"),
        )
        coEvery { getProductsUseCase.invoke() } returns Result.success(products)

        // When
        viewModel = ProductScreenViewmodel(getProductsUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(ProductState(), awaitItem()) // Default
            
            this@runTest.runCurrent()
            assertEquals(ProductState(isLoading = true), awaitItem()) // Loading
            
            val successState = awaitItem()
            assertEquals(products, successState.products)
            assertFalse(successState.isLoading)
            assertNull(successState.error)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getproducts failure should update uiState with error message`() = runTest {
        // Given
        val errorMessage = "Something went wrong"
        coEvery { getProductsUseCase.invoke() } returns Result.failure(Exception(errorMessage))

        // When
        viewModel = ProductScreenViewmodel(getProductsUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(ProductState(), awaitItem()) // Default
            
            this@runTest.runCurrent()
            assertEquals(ProductState(isLoading = true), awaitItem()) // Loading
            
            val errorState = awaitItem()
            assertTrue(errorState.products.isEmpty())
            assertEquals(errorMessage, errorState.error)
            assertFalse(errorState.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
