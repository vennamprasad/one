package com.prasad.techinterview.data.di

import com.prasad.techinterview.data.repository.ProductRepositoryImpl
import com.prasad.techinterview.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsProductsRepo(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}
