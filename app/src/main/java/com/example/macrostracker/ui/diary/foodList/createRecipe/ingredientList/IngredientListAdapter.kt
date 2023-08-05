package com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.Food
import java.text.DecimalFormat

class IngredientListAdapter(): ListAdapter<Food, IngredientListAdapter.IngredientViewHolder>(
    DiffCallBack
) {

    var selectionTracker: SelectionTracker<Long>? = null

    class IngredientViewHolder(
        private val binding: FoodListItemBinding
    ): ViewHolder(binding.root){

        private val decimalFormat = DecimalFormat("#.##")
        private var currentFood: Food? = null

        val details
            get() = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): Long? = currentFood?.id

                override fun inSelectionHotspot(e: MotionEvent): Boolean {
                    return true
                }
            }

        fun bind(food: Food) {
            val resources = itemView.resources
            currentFood = food
            binding.foodNameAndDescription.text =
                resources.getString(R.string.food_name_brand, food.name, food.brand)
            binding.foodServing.text =
                resources.getString(R.string.formatted_macros_number, food.servingSize)
            binding.foodMacros.text =
                resources.getString(
                    R.string.formatted_entry_macros,
                    food.calories,
                    decimalFormat.format(food.protein),
                    decimalFormat.format(food.fat),
                    decimalFormat.format(food.carbs)
                )
        }

        companion object{
            fun create(parent: ViewGroup): IngredientViewHolder {
                return IngredientViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val current = getItem(position)
        selectionTracker?.let {
            holder.bind(current)
            holder.itemView.isActivated = it.isSelected(current.id)
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Food>() {

            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }
        }
    }

}