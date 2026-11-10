package com.mdxam.cars_expenses.di

import android.content.Context
import com.mdxam.cars_expenses.data.CarLogDao
import com.mdxam.cars_expenses.data.CarLogDataBase
import com.mdxam.cars_expenses.data.CarLogRepositoryImpl
import com.mdxam.cars_expenses.domain.CarLogRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindCarLogRepository(
        impl: CarLogRepositoryImpl
    ): CarLogRepository

    companion object {

        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context: Context
        ): CarLogDataBase{
            return CarLogDataBase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideCarDao(
            dataBase: CarLogDataBase
        ): CarLogDao{
            return dataBase.carLogDao()
        }
    }
}