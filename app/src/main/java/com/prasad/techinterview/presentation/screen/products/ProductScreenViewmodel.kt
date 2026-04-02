package com.prasad.techinterview.presentation.screen.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasad.techinterview.domain.model.Product
import com.prasad.techinterview.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewmodel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductState()
    )
    val uiState = _uiState.asStateFlow()

    init {
        getproducts()
    }

    fun getproducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getProductsUseCase.invoke().onSuccess { products ->
                _uiState.update {
                    it.copy(isLoading = false, products = products, error = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, products = emptyList(), error = error.message)
                }
            }
        }
    }
}

data class ProductState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
)