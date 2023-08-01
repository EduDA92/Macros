package com.example.macrostracker.ui.diary.foodList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.Food

class MyFoodsAdapter(
    private val navigate: (Long, String) -> Unit
) : ListAdapter<Food, MyFoodsAdapter.FoodViewHolder>(DiffCallBack) {

    class FoodViewHolder(
        private var binding: FoodListItemBinding,
        private val navigate: (Long, String) -> Unit
    ) : ViewHolder(binding.root) {

        private var currentFood: Food? = null

        init {
            itemView.setOnClickListener {
                currentFood?.let { food ->
                    navigate(food.id, food.name)
                }
            }
        }

        fun bind(food: Food) {
            val resources = itemView.resources
            currentFood = food
            binding.foodNameAndDescription.text =
                resources.getString(R.string.food_name_brand, food.name, food.brand)
            binding.foodServing.text = resources.getString(R.string.formatted_macros_number, food.servingSize)
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
                navigate: (Long, String) -> Unit
            ): FoodViewHolder {
                return FoodViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate
                )
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder.create(parent, navigate)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

}