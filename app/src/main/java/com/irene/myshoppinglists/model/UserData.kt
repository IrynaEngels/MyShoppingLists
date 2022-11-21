package com.irene.myshoppinglists.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserData(val friends: String? = null,
                    val products: String? = null)
