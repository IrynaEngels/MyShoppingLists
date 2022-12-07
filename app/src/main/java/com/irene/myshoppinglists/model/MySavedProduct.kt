package com.irene.myshoppinglists.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_product_table")
data class MySavedProduct(
    @PrimaryKey
    val name: String
)
