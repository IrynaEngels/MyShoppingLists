package com.irene.myshoppinglists.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

val dropDownOptions = mutableStateOf(listOf<String>())
val textFieldValue = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)
fun onDropdownDismissRequest() {
    dropDownExpanded.value = false
}

fun onValueChanged(value: TextFieldValue, products: List<String>) {
    dropDownExpanded.value = true
    textFieldValue.value = value
    dropDownOptions.value = products.filter { it.startsWith(value.text) && it != value.text }.take(3)
}

@Composable
fun TextFieldWithDropdownUsage(products: List<String>, getProductName: (name: TextFieldValue) -> Unit) {
    TextFieldWithDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        setValue = {
            onValueChanged(it, products)
            getProductName(it)
                   },
        onDismissRequest = ::onDropdownDismissRequest,
        dropDownExpanded = dropDownExpanded.value,
        list = dropDownOptions.value
    )
}