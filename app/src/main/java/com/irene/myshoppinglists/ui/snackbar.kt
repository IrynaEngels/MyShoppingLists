package com.irene.myshoppinglists.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration

suspend fun showSnackBar(scaffoldState: ScaffoldState, snackBarAction: SnackBarAction) {
    val message = if (snackBarAction == SnackBarAction.USER_ALREADY_EXISTS) "User with this username already exists" else "Space is not allowed in username"
    val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
        message = message,
        actionLabel = "OK",
        duration = SnackbarDuration.Short
    )
}

enum class SnackBarAction {
    USER_ALREADY_EXISTS, SPACE_NOT_ALLOWED_IN_USERNAME
}