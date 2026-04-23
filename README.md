# Live Coding Interview Guide: Clean Architecture + Compose

This guide provides a step-by-step blueprint for building a modern Android app from scratch during a technical interview. It follows the **Clean Architecture** pattern used in this repository.

---

## Phase 0: Setup & Setup Checklist
*Before coding, verify these are ready (usually provided in starter repos):*
- [ ] `MainApplication.kt` has `@HiltAndroidApp`.
- [ ] `AndroidManifest.xml` points to `.MainApplication`.
- [ ] `build.gradle` has Hilt, Retrofit, and Compose dependencies.

---

## The 8-Step Implementation Sequence

1. **DOMAIN**: `Product.kt` (Model)
2. **DOMAIN**: `ProductRepository.kt` (Interface)
3. **DATA**: `ProductDto.kt` + `toDomain()` mapping
4. **DATA**: `ApiService.kt` (Retrofit)
5. **DATA**: `NetworkModule.kt` (Hilt Provider)
6. **DOMAIN**: `GetProductsUseCase.kt`
7. **PRESENTATION**: `ProductViewModel.kt` (State + Logic)
8. **UI**: `ProductScreen.kt` (Compose)

---

## Phase 1: Domain Layer (The Truth)
```kotlin
data class Product(
    val id: Int,
    val title: String,
    val description: String
)
```

### 2. Define Repository Interface
```kotlin
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
}
```

---

## Phase 2: Data Layer (The Reality)
*Implement how the data is fetched and mapped.*

### 3. Create Remote DTOs (Data Transfer Objects)
```kotlin
data class ProductResponseDto(
    @SerializedName("products") val products: List<ProductDto>
)

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)

// Mapping Extension (Data Layer or Mapper Class)
fun ProductDto.toDomain() = Product(
    id = id,
    title = title,
    description = description
)
```

### 4. API Service (Retrofit)
```kotlin
interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponseDto>
}
```

### 5. Hilt Module Setup (CRITICAL)
*Most interviewers look for correct DI configuration here.*

#### A. Network Module (Providers)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
```

#### B. Repository Module (Bindings)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}
```

---

## Phase 3: Repository & Use Case Implementation
*Actual logic execution.*

### 6. Repository Implementation
```kotlin
class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                val data = response.body()?.products?.map { it.toDomain() } ?: emptyList()
                Result.success(data)
            } else { Result.failure(Exception("Error")) }
        } catch (e: Exception) { Result.failure(e) }
    }
}
```

### 7. Create Use Case
```kotlin
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke() = repository.getProducts()
}
```

---

## Phase 4: Presentation Layer (The View)
*Manage UI State and Logic.*

### 7. Define UI State
```kotlin
data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
```

### 8. Create ViewModel (Hilt)
```kotlin
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState = _uiState.asStateFlow()

    init { getProducts() }

    fun getProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getProductsUseCase().onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, products = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
```

---

## Phase 5: UI Layer (Jetpack Compose)
*Declarative UI.*

### 10. Build the Screen
```kotlin
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        
        if (state.error != null) {
            Text(
                text = state.error, 
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyColumn(Modifier.fillMaxSize()) {
            items(state.products, key = { it.id }) { product ->
                ProductItem(product)
            }
        }
    }
}
```

---

---

## Phase 6: Bonus for "Seniority" (The Wow Factor)
*If you have time, add these to stand out.*

### 10. Image Loading with Coil
```kotlin
AsyncImage(
    model = product.thumbnail,
    contentDescription = null,
    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)),
    contentScale = ContentScale.Crop
)
```

### 11. Pull-to-Refresh (Material 3)
```kotlin
val pullToRefreshState = rememberPullToRefreshState()
PullToRefreshBox(
    isRefreshing = state.isLoading,
    onRefresh = { viewModel.getProducts() },
    state = pullToRefreshState
) {
    LazyColumn { ... }
}
```

### 12. Shimmer Effect
Instead of a simple `CircularProgressIndicator`, create a skeleton screen for better UX.

---

## Technical Interview Questions to Prepare
1. **Why Clean Architecture?** Separation of concerns, testability, and independence from frameworks.
2. **Why Use Cases?** To encapsulate single business logic units and reuse them across different ViewModels.
3. **MVI vs MVVM?** MVVM is state-based (MutableStateFlow), MVI is intent-based. This repo uses a "State-based MVVM" approach.
4. **Hilt vs Koin?** Hilt is compile-time safe and official; Koin is runtime and lightweight.
