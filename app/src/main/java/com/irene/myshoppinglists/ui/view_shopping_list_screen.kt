package com.irene.myshoppinglists.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.utils.*

@Composable
fun ShoppingListEditScreen(productListViewModel: ProductListViewModel, shoppingListId: String) {
    val allShoppingList = productListViewModel.shoppingLists.collectAsState()
    val products = allShoppingList.value.getListById(shoppingListId).products?.parseString()

    val allFriends = productListViewModel.userFriends.collectAsState()
    val editors = allShoppingList.value.getListById(shoppingListId).editors?.parseString() ?: listOf()

    var openDialog by remember { mutableStateOf(false) }
    if (openDialog)
        AddProductDialog({
            productListViewModel.addProductToDB(shoppingListId, it, products!!)
        }, {
            openDialog = false
        })

    var openFriendsDialog by remember { mutableStateOf(false) }
    if (openFriendsDialog)
        AddFriendsDialog(productListViewModel,
            allFriends.value.friendsNotAddedToThisList(editors), {
                productListViewModel.addFriendsInListToDB(shoppingListId, it, editors)
                openFriendsDialog = false
                productListViewModel.clearFriendsList()
            }, {
                openFriendsDialog = false
                productListViewModel.clearFriendsList()
            })

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Greeting("ProductsScreen")
        Text("Users who can edit this list")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AddFriendSharingItem() { openFriendsDialog = true }
            Spacer(modifier = Modifier.width(8.dp))
            for (i in editors!!) {
                FriendSharingItem(i) {}
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        AddProduct {
            openDialog = true
        }
        products?.let {
            for (p in products) {
                EditProductItem(product = p, {
                    productListViewModel.editProductsInDB(shoppingListId, it, products)
                }, {
                    productListViewModel.deleteProductFromDB(shoppingListId, it, products)
                })
            }
        }

    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun EditProductItem(
    product: String,
    edit: (product: String) -> Unit,
    delete: (product: String) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                edit(product)
            }) {
                if (product.isProductBought())
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Bought",
                    )
                else
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "ToBuy",
                    )
            }
            Spacer(modifier = Modifier.width(16.dp))
//            Text(product, fontSize = 16.sp)
            Text(
                product.showProductName(),
                fontSize = 16.sp,
                style = TextStyle(textDecoration = if (product.isProductBought()) TextDecoration.LineThrough else TextDecoration.None)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                delete(product)
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                )
            }
        }
        Divider()
    }
}