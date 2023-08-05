package com.example.macrostracker.ui.user.userRecipeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FoodListItemBinding
import com.example.macrostracker.model.RecipeWithFood
import java.text.DecimalFormat

class UserRecipeListAdapter(private val navigate:(String, Long) -> Unit) :
    ListAdapter<RecipeWithFood, UserRecipeListAdapter.UserRecipeViewHolder>(
        DiffCallBack
    ) {

    var selectionTracker: SelectionTracker<Long>? = null

    class UserRecipeViewHolder(
        private var binding: FoodListItemBinding,
        private val navigate:(String, Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentRecipe: RecipeWithFood? = null
        private val decimalFormat = DecimalFormat("#.##")

        init{
            itemView.setOnClickListener {
                currentRecipe?.let{
                    navigate(it.name, it.id)
                }
            }
        }

        val details
            get() = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): Long? = currentRecipe?.id

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
                navigate: (String, Long) -> Unit
            ): UserRecipeViewHolder {
                return UserRecipeViewHolder(
                    FoodListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRecipeViewHolder {
        return UserRecipeViewHolder.create(parent, navigate)
    }

    override fun onBindViewHolder(holder: UserRecipeViewHolder, position: Int) {
        val current = getItem(position)
        selectionTracker?.let {
            holder.bind(current)
            holder.itemView.isActivated = it.isSelected(current.id)
        }
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