package com.calorieai.app.di

import com.calorieai.app.data.repository.AnalysisRepository
import com.calorieai.app.data.repository.MealRepository
import com.calorieai.app.data.repository.StubAnalysisRepository
import com.calorieai.app.data.repository.StubMealRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: StubMealRepository): MealRepository

    @Binds
    @Singleton
    abstract fun bindAnalysisRepository(impl: StubAnalysisRepository): AnalysisRepository
}
