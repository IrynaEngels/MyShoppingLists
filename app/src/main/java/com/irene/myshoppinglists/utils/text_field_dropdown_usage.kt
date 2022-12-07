package com.irene.myshoppinglists.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun TextFieldWithDropdownUsage(products: List<String>, getProductName: (name: TextFieldValue) -> Unit) {
    val dropDownOptions = remember { mutableStateOf(listOf<String>()) }
    val textFieldValue = remember {mutableStateOf(TextFieldValue())}
    val dropDownExpanded = remember {mutableStateOf(false)}
    fun onDropdownDismissRequest() {
        dropDownExpanded.value = false
    }
    TextFieldWithDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        setValue = {textFieldVal ->
            dropDownExpanded.value = true
            textFieldValue.value = textFieldVal
            dropDownOptions.value = products.filter { it.startsWith(textFieldVal.text) && it != textFieldVal.text }.take(3)
            getProductName(textFieldVal)
                   },
        onDismissRequest = ::onDropdownDismissRequest,
        dropDownExpanded = dropDownExpanded.value,
        list = dropDownOptions.value
    )
}