package com.irene.myshoppinglists.ui

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun UsernameTextField(modifier: Modifier, setUsername: (name:
                                                        TextFieldValue) -> Unit) {
    val hint = "Create username"
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { newText ->
            text = newText
            setUsername(newText)
        },
        placeholder = {
            Text(text = hint, fontSize = 12.sp)
        }

    )
}