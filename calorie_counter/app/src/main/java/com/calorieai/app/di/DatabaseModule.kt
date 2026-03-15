package com.calorieai.app.di

import android.content.Context
import androidx.room.Room
import com.calorieai.app.data.local.AppDatabase
import com.calorieai.app.data.local.MealDao
import com.calorieai.app.data.local.MealFoodItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "calorie_counter.db"
        ).build()

    @Provides
    @Singleton
    fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()

    @Provides
    @Singleton
    fun provideMealFoodItemDao(db: AppDatabase): MealFoodItemDao = db.mealFoodItemDao()
}
