package com.calorieai.app.di

import com.calorieai.app.data.repository.AnalysisRepository
import com.calorieai.app.data.repository.DefaultAnalysisRepository
import com.calorieai.app.data.repository.DefaultMealRepository
import com.calorieai.app.data.repository.MealRepository
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
    abstract fun bindMealRepository(impl: DefaultMealRepository): MealRepository

    @Binds
    @Singleton
    abstract fun bindAnalysisRepository(impl: DefaultAnalysisRepository): AnalysisRepository
}
