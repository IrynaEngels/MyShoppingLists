package com.irene.myshoppinglists.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context = context

}