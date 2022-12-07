package com.irene.myshoppinglists.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.irene.myshoppinglists.model.MySavedProduct

@Database(entities = [MySavedProduct::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private var instance: ProductsDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ProductsDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, ProductsDatabase::class.java,
                    "products_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }
    }



}
