package com.irene.myshoppinglists.utils

import com.irene.myshoppinglists.model.Product
import com.irene.myshoppinglists.model.ShoppingList
import com.irene.myshoppinglists.model.ShoppingListWithId
import kotlinx.coroutines.flow.StateFlow


fun List<String>.isSuchUserExists(
    name: String
): Boolean {
    for (d in this)
        if (d == name) {
            return true
        }
    return false
}

fun String.parseString(): List<String> {
    return this.split(",")
}

fun String.formListAddString(list: List<String>): String {
    var listToString = ""
    for (f in list) {
        listToString += "$f,"
    }
    listToString += this
    return listToString
}

fun List<String>.formStringFromList(): String {
    var listToString = ""
    for (f in this) {
        listToString += "$f,"
    }
    return listToString.substring(0, listToString.length - 1)
}

fun String.excludeFromList(list: List<String>): String {
    var listToString = ""
    for (f in list) {
        if (f != this)
            listToString += "$f,"
    }
    return listToString.substring(0, listToString.length - 1)
}

fun String.editListWhenProductBought(list: List<String>): String {
    var listToString = ""
    for (f in list) {
        if (f == this)
            listToString += "${f.changeBoughtProperty()},"
        else
            listToString += "$f,"
    }
    return listToString.substring(0, listToString.length - 1)
}

fun String.changeBoughtProperty(): String {
    val name = this.subSequence(0, this.length-1).toString()
    val isBought = this.isProductBought()
    return if (!isBought)  "${name}B" else "${name}N"
 }

fun String.isProductBought(): Boolean {
    return this[this.length-1] == "B".first()
}


fun ShoppingList.createShoppingListWithId(id: String): ShoppingListWithId =
    ShoppingListWithId(id, this.name, this.editors, this.products)

fun List<ShoppingListWithId>.getListById(id: String): ShoppingListWithId {
    for (list in this)
        if (list.id == id)
            return list
    return ShoppingListWithId()
}

fun String.createNewProduct(): String {
    log("createNewProduct $this")
    return "${this}N"
}

fun String.showProductName(): String {
    return this.subSequence(0, this.length-1).toString()
}
