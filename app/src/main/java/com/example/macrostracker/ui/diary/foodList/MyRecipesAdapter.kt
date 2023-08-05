package com.example.macrostracker.ui.diary.foodList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.RecipeWithFood
import java.text.DecimalFormat

class MyRecipesAdapter(
    private val showDialog: (Long) -> Unit,
    private val navigate: (Long, String) -> Unit
) : ListAdapter<RecipeWithFood, MyRecipesAdapter.RecipeViewHolder>(
    DiffCallBack
) {

    class RecipeViewHolder(
        private var binding: FoodListItemBinding,
        private val navigate: (Long, String) -> Unit,
        private val showDialog: (Long) -> Unit
    ) : ViewHolder(binding.root) {

        private var currentRecipe: RecipeWithFood? = null
        private val decimalFormat = DecimalFormat("#.##")

        init {
            itemView.setOnClickListener {
                currentRecipe?.let { recipeWithFood ->
                    navigate(recipeWithFood.id, recipeWithFood.name)
                }
            }

            itemView.setOnLongClickListener{
                currentRecipe?.let{recipeWithFood ->
                showDialog(recipeWithFood.id)
                }
                true
            }
        }

        fun bind(recipeWithFood: RecipeWithFood) {
            val resources = itemView.resources
            currentRecipe = recipeWithFood
            binding.foodNameAndDescription.text = recipeWithFood.name
            binding.foodServing.text = recipeWithFood.servingSize.toString()
            binding.foodMacros.text = resources.getString(
                R.string.formatted_entry_macros,
                recipeWithFood.calories,
                decimalFormat.format(recipeWithFood.protein),
                decimalFormat.format(recipeWithFood.fat),
                decimalFormat.format(recipeWithFood.carbs)
            )
        }


        companion object {
            fun create(
                parent: ViewGroup,
                navigate: (Long, String) -> Unit,
                showDialog: (Long) -> Unit
            ): RecipeViewHolder {
                return RecipeViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate, showDialog
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.create(parent, navigate,showDialog)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<RecipeWithFood>() {

            override fun areItemsTheSame(
                oldItem: RecipeWithFood,
                newItem: RecipeWithFood
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RecipeWithFood,
                newItem: RecipeWithFood
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}