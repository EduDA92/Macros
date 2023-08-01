package com.example.macrostracker.ui.user.userFoodList

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class FoodDetailsLookup(private val recyclerView: RecyclerView): ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view: View? = recyclerView.findChildViewUnder(e.x, e.y)
        if(view != null){
            val holder = recyclerView.getChildViewHolder(view)
            if(holder is UserFoodListAdapter.UserFoodViewHolder){
                return holder.details
            }
        }
        return null
    }
}