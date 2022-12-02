package com.irene.myshoppinglists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.ui.navigation.CREATE_LIST_SCREEN
import com.irene.myshoppinglists.ui.navigation.EDIT_LIST_SCREEN
import com.irene.myshoppinglists.ui.navigation.navigateTo
import com.irene.myshoppinglists.utils.StoreUserData

@Composable
fun ListScreen(productListViewModel: ProductListViewModel, dataStore: StoreUserData, navController: NavHostController){

    val userName = dataStore.getName.collectAsState(initial = "")
    val shoppingLists = productListViewModel.shoppingLists.collectAsState()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())) {
        Greeting("${userName.value}'s ListScreen")
        Divider()
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(
                    onClick = {
                        navController.navigateTo(CREATE_LIST_SCREEN)
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )) {
            Icon(Icons.Outlined.Add, "add")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add List",
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
        Divider()
        for (list in shoppingLists.value){
            list.name?.let {
                ShoppingListItem(list.name, {
                    navController.navigate("$EDIT_LIST_SCREEN/${list.id}") {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, {
                    list.id?.let { id ->
                        productListViewModel.deleteList(id)
                    }

                })
            }

        }
    }
}

@Composable
fun ShoppingListItem(shoppingListName: String, openList: () -> Unit, delete: () -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = {
                    openList()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )) {
        Icon(Icons.Outlined.List, "list")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = shoppingListName,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            delete()
        }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Add",
            )
        }
    }
    Divider()
}