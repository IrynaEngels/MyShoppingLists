package com.irene.myshoppinglists.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingList(
    val name: String? = null,
    val editors: String? = null,
    val products: String? = null
)

data class ShoppingListWithId(
    val id: String? = null,
    val name: String? = null,
    val editors: String? = null,
    val products: String? = null
)
