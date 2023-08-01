package com.example.macrostracker.ui.user.userFoodList

import androidx.recyclerview.selection.ItemKeyProvider

class FoodKeyProvider(private val adapter: UserFoodListAdapter) :
    ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long =
        adapter.currentList[position].id

    override fun getPosition(key: Long): Int =
        adapter.currentList.indexOfFirst { food -> food.id == key }

}