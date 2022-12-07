package com.irene.myshoppinglists.di

import android.content.Context
import androidx.room.Room
import com.irene.myshoppinglists.database.ProductDao
import com.irene.myshoppinglists.database.ProductsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideProductDao(database: ProductsDatabase): ProductDao {
        return database.productDao()
    }
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ProductsDatabase {
        return Room.databaseBuilder(
            appContext,
            ProductsDatabase::class.java,
            "RssReader"
        ).build()
    }
}
