package com.irene.myshoppinglists.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irene.myshoppinglists.firebase.FirebaseRepository
import com.irene.myshoppinglists.model.ShoppingList
import com.irene.myshoppinglists.model.ShoppingListWithId
import com.irene.myshoppinglists.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {
    private val _products: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf())

    val productsStateFlow: StateFlow<List<String>> = _products.asStateFlow()

    val shoppingLists = repository.shoppingListsStateFlow

    fun addProduct(product: String) {
        val list = mutableListOf<String>()
        for (p in productsStateFlow.value)
            list.add(p)
        list.add(product)
        _products.value = list
    }

    fun addProductToDB(id: String, newProduct: String, products: List<String>) {
        repository.editProducts(id, newProduct.formListAddString(products))
    }

    fun deleteProductFromDB(id: String, product: String, products: List<String>) {
        repository.editProducts(id, product.excludeFromList(products))
    }

    fun editProductsInDB(id: String, product: String, products: List<String>) {
        repository.editProducts(id, product.editListWhenProductBought(products))
    }

    fun deleteProduct(product: String) {
        val list = mutableListOf<String>()
        for (p in productsStateFlow.value)
            if (p != product)
                list.add(p)
        _products.value = list
    }

    fun createList(context: Context, name: String, friends: List<String>, products: List<String>) {
        val storeUserData = StoreUserData(context)
        viewModelScope.launch {
            val editors = mutableListOf<String>()
            for (f in friends)
                editors.add(f)
            storeUserData.getName.collect {
                it?.let {
                    editors.add(it)
                    repository.writeNewShoppingList(
                        ShoppingList(
                            name,
                            editors.formStringFromList(),
                            products.formStringFromList()
                        )
                    )
                }
            }
        }
    }

}
