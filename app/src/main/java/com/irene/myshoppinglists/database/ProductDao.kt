package com.irene.myshoppinglists.database

import androidx.room.*
import androidx.room.FtsOptions.Order
import com.irene.myshoppinglists.model.MySavedProduct
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {
    @Update
    suspend fun update(device: MySavedProduct)

    @Query("DELETE FROM my_product_table WHERE name = :name")
    suspend fun deleteProduct(name: String)

    @Query("select * from my_product_table")
    fun getAllProducts(): Flow<List<MySavedProduct>>

    @Query("SELECT * FROM my_product_table WHERE name = :name")
    fun getProduct(name: String): Flow<MySavedProduct>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProductsList(products: List<MySavedProduct>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: MySavedProduct)

}