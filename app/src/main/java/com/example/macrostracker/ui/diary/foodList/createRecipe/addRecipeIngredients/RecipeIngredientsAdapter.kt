package com.example.macrostracker.ui.diary.foodList.createRecipe.addRecipeIngredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.FoodWithQuantity
import java.text.DecimalFormat

class RecipeIngredientsAdapter(private val navigate: (Long) -> Unit) :
    ListAdapter<FoodWithQuantity, RecipeIngredientsAdapter.IngredientViewHolder>(
        DiffCallBack
    ) {

    class IngredientViewHolder(
        private var binding: FoodListItemBinding,
        private val navigate: (Long) -> Unit
    ) : ViewHolder(binding.root) {

        private val decimalFormat = DecimalFormat("#.##")
        private var currentFood: FoodWithQuantity? = null

        init{
            itemView.setOnLongClickListener {
                currentFood?.let{
                    navigate(it.food.id)
                }
                true
            }
        }
        fun bind(foodWithQuantity: FoodWithQuantity) {
            val resources = itemView.resources
            currentFood = foodWithQuantity
            binding.foodNameAndDescription.text =
                resources.getString(
                    R.string.food_name_brand,
                    foodWithQuantity.food.name,
                    foodWithQuantity.food.brand
                )
            binding.foodServing.text =
                resources.getString(R.string.formatted_macros_number, foodWithQuantity.quantity)
            binding.foodMacros.text =
                resources.getString(
                    R.string.formatted_entry_macros,
                    foodWithQuantity.calories,
                    decimalFormat.format(foodWithQuantity.protein),
                    decimalFormat.format(foodWithQuantity.fat),
                    decimalFormat.format(foodWithQuantity.carbs)
                )
        }

        companion object {
            fun create(parent: ViewGroup, navigate: (Long) -> Unit): IngredientViewHolder {
                return IngredientViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.create(parent, navigate)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<FoodWithQuantity>() {

            override fun areItemsTheSame(
                oldItem: FoodWithQuantity,
                newItem: FoodWithQuantity
            ): Boolean {
                return oldItem.food.id == newItem.food.id
            }

            override fun areContentsTheSame(
                oldItem: FoodWithQuantity,
                newItem: FoodWithQuantity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}