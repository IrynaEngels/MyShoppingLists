package com.irene.myshoppinglists.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.utils.*

@Composable
fun ShoppingListEditScreen(productListViewModel: ProductListViewModel, shoppingListId: String){
    val allShoppingList = productListViewModel.shoppingLists.collectAsState()
    val products = allShoppingList.value.getListById(shoppingListId).products?.parseString()

    var openDialog by remember { mutableStateOf(false) }
    if (openDialog)
        AddProductDialog({
            productListViewModel.addProductToDB(shoppingListId, it, products!!)
        }, {
            openDialog = false
        })

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())) {
        Greeting("ProductsScreen")
        AddProduct {
            openDialog = true
        }
        products?.let {
            for (p in products){
                EditProductItem(product = p, {
                    productListViewModel.editProductsInDB(shoppingListId, it, products)
                }, {
                    productListViewModel.deleteProductFromDB(shoppingListId, it, products)
                })
            }
        }

    }
}

@Composable
fun EditProductItem(product: String, edit: (product: String) -> Unit, delete: (product: String) -> Unit){
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                edit(product)
            }) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "ToBuy",
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(product, fontSize = 16.sp)
//            Text(product.showProductName(), fontSize = 16.sp)
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