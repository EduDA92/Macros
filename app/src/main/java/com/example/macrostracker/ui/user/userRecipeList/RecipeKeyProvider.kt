package com.example.macrostracker.ui.user.userRecipeList

import androidx.recyclerview.selection.ItemKeyProvider

class RecipeKeyProvider(private val adapter: UserRecipeListAdapter) : ItemKeyProvider<Long>(
    SCOPE_CACHED
) {

    override fun getKey(position: Int): Long? {
        return adapter.currentList[position].id
    }

    override fun getPosition(key: Long): Int {
        return adapter.currentList.indexOfFirst { it.id == key }
    }
}