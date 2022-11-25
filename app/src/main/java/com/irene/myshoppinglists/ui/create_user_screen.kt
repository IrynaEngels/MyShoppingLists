package com.irene.myshoppinglists.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.ui.navigation.BottomNavItem
import com.irene.myshoppinglists.ui.navigation.CREATE_USER_SCREEN
import com.irene.myshoppinglists.utils.StoreUserData
import com.irene.myshoppinglists.utils.isSuchUserExists
import com.irene.myshoppinglists.utils.isUsernameAllowed
import com.irene.myshoppinglists.utils.log
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateUserScreen(
    firebaseRepository: FirebaseRepository,
    dataStore: StoreUserData,
    navController: NavHostController,
    showSnackBar: suspend (snackBarAction: SnackBarAction) -> Unit
) {
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val users = firebaseRepository.usersStateFlow.collectAsState(initial = listOf())

    val scope = rememberCoroutineScope()

    Column() {
        Greeting("Android")
        UsernameTextField(modifier = Modifier.fillMaxWidth()) {
            userName = it
        }
        Button(
            onClick = {
                log(userName.text)
                val userExists = users.value.isSuchUserExists(userName.text)
                if (!userExists && userName.text.isUsernameAllowed()){
                    keyboardController?.hide()
                    navController.navigate(BottomNavItem.Lists.screen_route) {
                        popUpTo(CREATE_USER_SCREEN) {
                            inclusive = true
                        }
                    }
                }
                scope.launch {
                    keyboardController?.hide()
                    if (!userExists) {
                        if (userName.text.isUsernameAllowed()) {
                            firebaseRepository.writeNewUser(userName.text)
                            dataStore.saveName(userName.text)
                        } else {
                            showSnackBar(SnackBarAction.SPACE_NOT_ALLOWED_IN_USERNAME)
                        }
                    } else {
                        showSnackBar(SnackBarAction.USER_ALREADY_EXISTS)
                    }
                }
            }
        ) {
            Text(text = "Create user")
        }
    }
}
