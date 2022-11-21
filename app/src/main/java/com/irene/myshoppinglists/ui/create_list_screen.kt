package com.irene.myshoppinglists.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.utils.createNewProduct

@Composable
fun CreateListScreen(productListViewModel: ProductListViewModel) {
    val items = (1..10).map { "Item $it" }
    val context = LocalContext.current
    val products = productListViewModel.productsStateFlow.collectAsState()

    var listName by remember { mutableStateOf("List") }
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog)
        AddProductDialog({
            productListViewModel.addProduct(it)
        }, {
            openDialog = false
        })

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Name")
        ListNameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            listName = it
        }
        Text("Friends who can edit this list")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AddFriendSharingItem() {}
            Spacer(modifier = Modifier.width(8.dp))
            for (i in items) {
                FriendSharingItem(i) {}
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text("Products")
        AddProduct {
            openDialog = true
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            state = rememberLazyListState()
        ) {
            itemsIndexed(products.value) { index, item ->
                ProductItem(item) {
                    productListViewModel.deleteProduct(it)
                }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, end = 16.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    productListViewModel.createList(
                        context,
                        listName,
                        listOf(),
                        products.value
                    )
                }
            ) {
                Text("Save List")
            }
        }
    }
}

@Composable
fun ListNameTextField(
    modifier: Modifier,
    setListName: (name: String) -> Unit
) {
    val hint = "Enter list name"
    var text by remember { mutableStateOf(TextFieldValue("List")) }
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = { newText ->
            text = newText
            setListName(newText.text)
        },
        placeholder = {
            Text(text = hint, fontSize = 12.sp)
        },
        trailingIcon = {
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Set",
                )
            }
        }

    )
}

@Composable
fun FriendSharingItem(
    name: String,
    delete: (name: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(name)
                IconButton(onClick = {
                    delete(name)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        }
    }
}

@Composable
fun AddFriendSharingItem(
    addFromList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))
                Text("Add friend")
                IconButton(onClick = {
                    addFromList()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Delete",
                    )
                }
            }
        }
    }
}

@Composable
fun AddProduct(openAddDialog: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = {
                    openAddDialog()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Icon(Icons.Outlined.Add, "add")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Add Product",
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
    Divider()
}

@Composable
fun ProductItem(product: String, delete: (product: String) -> Unit) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(product, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                delete(product)
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Add",
                )
            }
        }
        Divider()
    }
}

@Composable
fun AddProductDialog(addProduct: (product: String) -> Unit, closeDialog: () -> Unit) {
    var product by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
            Spacer(modifier = Modifier.height(4.dp))
        },
        text = {
            ProductTextField(modifier = Modifier.padding(all = 8.dp)) {
                product = it.text
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { closeDialog() }
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        addProduct(product.createNewProduct())
                        closeDialog()
                    }
                ) {
                    Text("Add")
                }
            }
        }
    )
}