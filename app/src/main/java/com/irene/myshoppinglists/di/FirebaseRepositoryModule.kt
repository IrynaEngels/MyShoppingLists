package com.irene.myshoppinglists.di

import com.irene.myshoppinglists.database.ProductDao
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.utils.StoreUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseRepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        storeUserData: StoreUserData,
        productDao: ProductDao
    ): FirebaseRepository {
        return FirebaseRepository(storeUserData, productDao)
    }
}
