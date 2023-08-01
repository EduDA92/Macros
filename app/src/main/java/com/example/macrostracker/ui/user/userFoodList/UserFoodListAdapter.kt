package com.example.macrostracker.ui.user.userFoodList

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.Food

class UserFoodListAdapter(private val navigate: (String, Long) -> Unit) :
    ListAdapter<Food, UserFoodListAdapter.UserFoodViewHolder>(DiffCallBack) {

    var selectionTracker: SelectionTracker<Long>? = null

    class UserFoodViewHolder(
        private var binding: FoodListItemBinding,
        private val navigate: (String, Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentFood: Food? = null

        init {
            itemView.setOnClickListener {
                currentFood?.let { food ->
                    navigate(food.name, food.id)
                }
            }
        }

        val details
            get() = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): Long? = currentFood?.id

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
                    food.protein,
                    food.fat,
                    food.carbs
                )
        }

        companion object {
            fun create(
                parent: ViewGroup,
                navigate: (String, Long) -> Unit
            ): UserFoodViewHolder {
                return UserFoodViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFoodViewHolder {
        return UserFoodViewHolder.create(parent, navigate)
    }

    override fun onBindViewHolder(holder: UserFoodViewHolder, position: Int) {
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