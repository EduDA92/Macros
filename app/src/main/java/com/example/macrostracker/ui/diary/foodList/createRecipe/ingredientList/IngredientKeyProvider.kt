package com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList

import androidx.recyclerview.selection.ItemKeyProvider

class IngredientKeyProvider(private val adapter: IngredientListAdapter) :
    ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long =
        adapter.currentList[position].id

    override fun getPosition(key: Long): Int =
        adapter.currentList.indexOfFirst { food -> food.id == key }

}