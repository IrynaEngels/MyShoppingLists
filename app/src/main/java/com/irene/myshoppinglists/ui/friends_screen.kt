package com.irene.myshoppinglists.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.ui.theme.SearchFriendTextField
import com.irene.myshoppinglists.utils.isSuchUserExists
import com.irene.myshoppinglists.utils.log
import com.irene.myshoppinglists.utils.removeWhitespaces
import kotlinx.coroutines.launch

@Composable
fun FriendsScreen(firebaseRepository: FirebaseRepository){
    val scope = rememberCoroutineScope()

    val users = firebaseRepository.usersStateFlow.collectAsState(initial = listOf())
    var userExists by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var userSearchClicked by remember { mutableStateOf(false) }

    val friends = firebaseRepository.friendsStateFlow.collectAsState(initial = listOf())

    Column(modifier = Modifier.padding(16.dp)) {
        SearchFriendTextField(modifier = Modifier.fillMaxWidth()){
            userExists = users.value.isSuchUserExists(it.text.removeWhitespaces())
            if (userExists) userName = it.text
            log("userExists $userExists")
            userSearchClicked = true
        }
        if (userSearchClicked){
            if (userExists){
                if (friends.value.contains(userName)){
                    Text("$userName is already your friend!")
                } else {
                    AddUser(userName){ name ->
                        scope.launch {
                            firebaseRepository.addFriend(name)
                        }
                    }
                }
            } else {
                Text("No such user")
            }
        }
        for (f in friends.value){
            FriendItem(userName = f){
                scope.launch {
                    firebaseRepository.deleteFriend(it)
                }
            }
        }
    }
}

@Composable
fun AddUser(userName: String, addUserToFriends: (userName: String) -> Unit){
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(userName, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                addUserToFriends(userName)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
        }
        Divider()
    }

}

@Composable
fun FriendItem(userName: String, delete: (userName: String) -> Unit){
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(userName, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                delete(userName)
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