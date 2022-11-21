package com.irene.myshoppinglists.ui.theme

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun SearchFriendTextField(modifier: Modifier,
                          checkIfUserExists: (name: TextFieldValue) -> Unit) {
    val hint = "Enter friend's username"
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        placeholder = {
            Text(text = hint, fontSize = 12.sp)
        },
        trailingIcon = {
            IconButton(onClick = {
                checkIfUserExists(text)
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                )
            }
        }

    )
}