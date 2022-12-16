package com.irene.myshoppinglists.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.utils.*

@Composable
fun ShoppingListEditScreen(productListViewModel: ProductListViewModel, shoppingListId: String) {
    val allShoppingList = productListViewModel.shoppingLists.collectAsState()
    val products = allShoppingList.value.getListById(shoppingListId).products?.parseString()

    val allFriends = productListViewModel.userFriends.collectAsState()
    val editors = allShoppingList.value.getListById(shoppingListId).editors?.parseString() ?: listOf()

    var openDialog by remember { mutableStateOf(false) }
    if (openDialog)
        AddProductDialog(productListViewModel, {
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
    var openQuantityDialog by remember { mutableStateOf(false) }
    var productToChangeQuantity by remember { mutableStateOf("") }
    if (openQuantityDialog)
        AddQuantityDialog(productListViewModel, productToChangeQuantity, {quantity, name ->
            productListViewModel.productInDBSetQuantity(shoppingListId, name, quantity, products!!)
            openQuantityDialog = false
            }, {
            openQuantityDialog = false
            })
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
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
                FriendSharingItem(i) {
                    productListViewModel.deleteFriendInListFromDB(shoppingListId, it, editors)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        AddProduct {
            openDialog = true
        }
        products?.let {
            for (p in products) {
                EditProductItem(product = p,  { product ->
                    productToChangeQuantity = product
                    openQuantityDialog = true
                },{
                    productListViewModel.productInDBSetBought(shoppingListId, it, products)
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
    changeQuantity: (product: String) -> Unit,
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .width(28.dp)
                    .height(28.dp)
                    .background(Color.LightGray)
                    .border(width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(4.dp))
                    .clickable {
                        changeQuantity(product)
                    }, contentAlignment = Alignment.Center
            ){
                Text(
                    "${product.showProductQuantity()}",
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                product.showProductName(),
                modifier = Modifier.width(200.dp),
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
