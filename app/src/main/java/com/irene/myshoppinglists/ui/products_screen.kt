package com.irene.myshoppinglists.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.utils.isProductBought
import com.irene.myshoppinglists.utils.showProductName

@Composable
fun ProductsScreen(productListViewModel: ProductListViewModel){
    val myProducts = productListViewModel.getSavedProducts().collectAsState()
    Column() {
        Text("ProductsScreen")
        for (product in myProducts.value){
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        product,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        productListViewModel.deleteMySavedProduct(product)
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
    }
}