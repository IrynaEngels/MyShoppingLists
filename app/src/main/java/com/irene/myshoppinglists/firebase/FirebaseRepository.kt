package com.irene.myshoppinglists.firebase

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.irene.myshoppinglists.model.ShoppingList
import com.irene.myshoppinglists.model.ShoppingListWithId
import com.irene.myshoppinglists.model.User
import com.irene.myshoppinglists.model.UserData
import com.irene.myshoppinglists.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val storeUserData: StoreUserData
) {

    private lateinit var database: DatabaseReference

    private val _users: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf())

    val usersStateFlow: StateFlow<List<String>> = _users.asStateFlow()

    private val _friends: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf())

    val friendsStateFlow: StateFlow<List<String>> = _friends.asStateFlow()

    private val _shoppingLists: MutableStateFlow<List<ShoppingListWithId>> =
        MutableStateFlow(listOf())

    val shoppingListsStateFlow: StateFlow<List<ShoppingListWithId>> = _shoppingLists.asStateFlow()

    //TODO cancel
    private val scope = CoroutineScope(Dispatchers.IO)

    val userListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            log("onDataChange $dataSnapshot")
            if (dataSnapshot.hasChildren()) {
                val iter: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
                val list = mutableListOf<String>()
                for (i in iter) {
                    log("key: ${i.key}")
                    val user = i.getValue<User>()
                    user?.username?.let {
                        log(it)
                        list.add(it)
                    }
                }

                _users.value = list
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            log("loadPost:onCancelled ${databaseError.toException()}")
        }
    }

    val userDataListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            log("onDataChange $dataSnapshot")
            if (dataSnapshot.hasChildren()) {
                val iter: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
                val list = mutableListOf<String>()
                for (i in iter) {
                    log("key: ${i.key}")
                    //if key = user name
                    scope.launch {
                        storeUserData.getName.collect{ currentUser ->
                            log("currentUser $currentUser")
                            if (currentUser == i.key) {
                                val user = i.getValue<UserData>()
                                user?.friends?.let {
                                    if (it.isNotEmpty())
                                        for (friend in it.parseString()) {
                                            log("friend $friend")
                                            list.add(friend)
                                        }
                                }
                                _friends.value = list
                            }
                        }

                    }

                }

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            log("loadPost:onCancelled ${databaseError.toException()}")
        }
    }

    val shoppingListListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                val iter: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
                scope.launch {
                    storeUserData.getName.collect { user ->
                    val list = mutableListOf<ShoppingListWithId>()
                    for (i in iter) {
                        val key = i.key
                        val shoppingList = i.getValue<ShoppingList>()
                        shoppingList?.let {
                            it.editors?.let { editors ->
                                    user?.let {
                                        if (editors.parseString().isSuchUserExists(user)) {
                                            key?.let {
                                                list.add(shoppingList.createShoppingListWithId(key))
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        _shoppingLists.value = list
                    }
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            log("loadPost:onCancelled ${databaseError.toException()}")
        }
    }

    init {
        database = Firebase.database.reference
        database.child("users").addValueEventListener(userListener)
        database.child("user_data").addValueEventListener(userDataListener)
        database.child("lists").addValueEventListener(shoppingListListener)
    }

    suspend fun writeNewUser(name: String) {
        val user = User(name)

        val userKey = database.child("users").push()
        userKey.key?.let {
            storeUserData.saveID(it)
            log("key: $it")
        }
        userKey.setValue(user)
    }

    fun writeNewShoppingList(shoppingList: ShoppingList) {
        database.child("lists").push().setValue(shoppingList)
    }

    fun editProducts(id: String, products: String) {
        val reference =
            FirebaseDatabase.getInstance().getReference("lists").child(id)
        val hashMap: MutableMap<String, Any> = HashMap()
        hashMap["products"] = products
        reference.updateChildren(hashMap)
    }

    fun editFriends(id: String, friends: String) {
        val reference =
            FirebaseDatabase.getInstance().getReference("lists").child(id)
        val hashMap: MutableMap<String, Any> = HashMap()
        hashMap["friends"] = friends
        reference.updateChildren(hashMap)
    }

    suspend fun addFriend(friendName: String) {
        storeUserData.getID.collect { id ->
            id?.let {
                val reference =
                    FirebaseDatabase.getInstance().getReference("user_data").child(id)
                val hashMap: MutableMap<String, Any> = HashMap()
                hashMap["friends"] = friendName.formListAddString(_friends.value)
                reference.updateChildren(hashMap)
            }
        }
    }

    suspend fun deleteFriend(friendName: String) {
        storeUserData.getID.collect { id ->
            id?.let {
                val reference =
                    FirebaseDatabase.getInstance().getReference("user_data").child(id)
                val hashMap: MutableMap<String, Any> = HashMap()
                hashMap["friends"] = friendName.excludeFromList(_friends.value)
                reference.updateChildren(hashMap)
            }
        }
    }

    fun cancel() {
        scope.cancel()
    }

}