package com.irene.myshoppinglists.di

import android.content.Context
import com.irene.myshoppinglists.utils.StoreUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StoreUserDataModule {

    @Provides
    @Singleton
    fun provideStoreUserData(
        @ApplicationContext context: Context
    ): StoreUserData {
        return StoreUserData(context)
    }
}
