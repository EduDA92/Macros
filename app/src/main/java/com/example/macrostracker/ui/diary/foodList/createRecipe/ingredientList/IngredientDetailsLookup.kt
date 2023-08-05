package com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class IngredientDetailsLookup(private val recyclerView: RecyclerView): ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view: View? = recyclerView.findChildViewUnder(e.x, e.y)
            if(view != null){
                val holder = recyclerView.getChildViewHolder(view)
                if(holder is IngredientListAdapter.IngredientViewHolder){
                    return holder.details
                }
            }
            return null
        }
    }
