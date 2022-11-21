package com.irene.myshoppinglists.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.irene.myshoppinglists.utils.log
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateUserScreen(firebaseRepository: FirebaseRepository, dataStore: StoreUserData, navController: NavHostController){
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    Column() {
        Greeting("Android")
        UsernameTextField(modifier = Modifier.fillMaxWidth()){
            userName = it
        }
        Button(
            onClick = {
                log(userName.text)
                scope.launch {
                    firebaseRepository.writeNewUser(userName.text)
                    dataStore.saveName(userName.text)
                }
                keyboardController?.hide()
                navController.navigate(BottomNavItem.Lists.screen_route){
                    popUpTo(CREATE_USER_SCREEN) {
                        inclusive = true
                    }
                }
            }
        ){
            Text(text = "Create user")
        }
    }
}