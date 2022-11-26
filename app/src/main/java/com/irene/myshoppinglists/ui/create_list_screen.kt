package com.irene.myshoppinglists.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.navigation.NavHostController
import com.irene.myshoppinglists.Greeting
import com.irene.myshoppinglists.ui.navigation.BottomNavItem
import com.irene.myshoppinglists.ui.navigation.CREATE_LIST_SCREEN
import com.irene.myshoppinglists.ui.navigation.navigateTo
import com.irene.myshoppinglists.utils.createNewProduct
import com.irene.myshoppinglists.utils.log
import com.irene.myshoppinglists.utils.showProductName

@Composable
fun CreateListScreen(productListViewModel: ProductListViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val products = productListViewModel.productsStateFlow.collectAsState()
    val allFriends = productListViewModel.userFriends.collectAsState()
    val friendsInList = productListViewModel.friendsStateFlow.collectAsState()

    BackHandler(enabled = false) {
        productListViewModel.clearData()
    }

    var listName by remember { mutableStateOf("List") }
    var openProductDialog by remember { mutableStateOf(false) }
    if (openProductDialog)
        AddProductDialog({
            productListViewModel.addProduct(it)
        }, {
            openProductDialog = false
        })

    var openFriendsDialog by remember { mutableStateOf(false) }
    if (openFriendsDialog)
        AddFriendsDialog(productListViewModel, {
        }, {
            openFriendsDialog = false
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
            AddFriendSharingItem() { openFriendsDialog = true }
            Spacer(modifier = Modifier.width(8.dp))
            for (i in friendsInList.value) {
                FriendSharingItem(i) {}
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text("Products")
        AddProduct {
            openProductDialog = true
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
                    if (products.value.isNotEmpty()) {
                        productListViewModel.createList(
                            context,
                            listName,
                            friendsInList.value,
                            products.value
                        )
                        Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()

                        navController.navigateTo(BottomNavItem.Lists.screen_route)
                        productListViewModel.clearData()
                    }
                    else Toast.makeText(context, "Add some products to list", Toast.LENGTH_SHORT).show()
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
            Text(product.showProductName(), fontSize = 16.sp)
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


@Composable
fun AddFriendsDialog(productListViewModel: ProductListViewModel, addFriends: (friends: List<String>) -> Unit, closeDialog: () -> Unit) {

    val allFriends = productListViewModel.userFriends.collectAsState()
    val friendList = productListViewModel.friendsStateFlow.collectAsState()
    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
            Spacer(modifier = Modifier.height(4.dp))
        },
        text = {
            Column() {
                log("friendList ${friendList.value.size}")
                for (friend in allFriends.value) {
                    val isAdded = friendList.value.contains(friend)
                    AddFriendToList(friend, isAdded) {
                        if (isAdded) {
                            log("isAdded")
                            productListViewModel.removeFriend(it)
                        }
                        else {
                            log("is not added")
                            productListViewModel.addFriend(it)
                        }
                    }
                }
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
                    Text("OK")
                }
            }
        }
    )
}


@Composable
fun AddFriendToList(userName: String, isAdded: Boolean, addOrRemoveFriend: (userName: String) -> Unit){
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(userName, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                addOrRemoveFriend(userName)
            }) {
                if (isAdded)
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Add",
                    )
                else
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                    )
            }
        }
        Divider()
    }
}