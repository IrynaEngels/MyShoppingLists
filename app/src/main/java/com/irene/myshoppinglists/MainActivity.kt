package com.irene.myshoppinglists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.ui.*
import com.irene.myshoppinglists.ui.navigation.BottomNavigationBar
import com.irene.myshoppinglists.ui.navigation.NavigationGraph
import com.irene.myshoppinglists.ui.theme.MyShoppingListsTheme
import com.irene.myshoppinglists.utils.StoreUserData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//enable friends delete in view mode
// make room database with frequently used products

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    @Inject
    lateinit var storeUserData: StoreUserData

    private val productListViewModel: ProductListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {

            var showBottomBar by remember { mutableStateOf(true) }

            val navController = rememberNavController()
            val state: ScaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = state,
                bottomBar = {
                    if (showBottomBar)
                        BottomNavigationBar(navController = navController)
                }
            ) {
                NavigationGraph(
                    navController = navController,
                    { CreateUserScreen(firebaseRepository, storeUserData, navController){action ->
                        showSnackBar(state, action)
                    } },
                    { ListScreen(productListViewModel, storeUserData, navController) },
                    { FriendsScreen(firebaseRepository) },
                    { ProductsScreen() },
                    { CreateListScreen(productListViewModel, navController) },
                    { id -> ShoppingListEditScreen(productListViewModel, id) },
                    { showBottomBar = it}
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyShoppingListsTheme {
        Greeting("Android")
    }
}