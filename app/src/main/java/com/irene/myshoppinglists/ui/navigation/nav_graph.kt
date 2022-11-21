package com.irene.myshoppinglists.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.irene.myshoppinglists.utils.StoreUserData

const val CREATE_USER_SCREEN = "create_user_screen"
const val CREATE_LIST_SCREEN = "search_devices_screen"
const val EDIT_LIST_SCREEN = "edit_list_screen"
@Composable
fun NavigationGraph(navController: NavHostController,
                    createUserScreen: @Composable() () -> Unit,
                    listsScreen: @Composable() () -> Unit,
                    friendsScreen: @Composable() () -> Unit,
                    productsScreen: @Composable() () -> Unit,
                    createListScreen: @Composable() () -> Unit,
                    editListScreen: @Composable() (id: String) -> Unit,
                    showBottomBar: (macAddress: Boolean) -> Unit) {

    val context = LocalContext.current
    val dataStore = StoreUserData(context)
    val userName = dataStore.getName.collectAsState(initial = "")

    val startDestination = if (userName.value == "") CREATE_USER_SCREEN else BottomNavItem.Lists.screen_route

    NavHost(navController, startDestination = startDestination) {
        composable(CREATE_USER_SCREEN) {
            createUserScreen()
            showBottomBar(false)
        }
        composable(BottomNavItem.Lists.screen_route) {
            listsScreen()
            showBottomBar(true)
        }
        composable(BottomNavItem.Friends.screen_route) {
            friendsScreen()
            showBottomBar(true)
        }
        composable(BottomNavItem.Products.screen_route) {
            productsScreen()
            showBottomBar(true)
        }
        composable(CREATE_LIST_SCREEN) {
            createListScreen()
            showBottomBar(false)
        }
        composable(EDIT_LIST_SCREEN) {
            createListScreen()
            showBottomBar(false)
        }
        composable("$EDIT_LIST_SCREEN/{shoppingListId}") { navBackStackEntry ->
            val shoppingListId = navBackStackEntry.arguments?.getString("shoppingListId")
            shoppingListId?.let {
                editListScreen(shoppingListId)
            }
            showBottomBar(false)
        }
    }
}

fun NavHostController.navigateTo(path: String) {
    val navController = this
    navController.navigate(path) {

        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
