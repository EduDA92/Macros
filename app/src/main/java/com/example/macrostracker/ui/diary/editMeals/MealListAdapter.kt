package com.example.macrostracker.ui.diary.editMeals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.databinding.MealListItemBinding
import com.example.macrostracker.model.Food
import com.example.macrostracker.model.Meal

class MealListAdapter(private val updateMealVisibility: (Long, Boolean) -> Unit,
    private val navigateToMealNameDialog: (Long) -> Unit) :
    ListAdapter<Meal, MealListAdapter.MealListItemViewHolder>(DiffCallBack) {

    class MealListItemViewHolder(
        private var binding: MealListItemBinding,
        private val updateMealVisibility: (Long, Boolean) -> Unit,
        private val navigateToMealNameDialog: (Long) -> Unit
    ) : ViewHolder(binding.root) {

        private var currentMeal: Meal? = null

        init {
            binding.mealSwitch.setOnCheckedChangeListener { _, isChecked ->
                currentMeal?.let { meal ->
                    updateMealVisibility(meal.id, isChecked)
                }
            }

            binding.editButton.setOnClickListener {
                currentMeal?.let{meal ->
                    navigateToMealNameDialog(meal.id)
                }
            }
        }

        fun bind(meal: Meal) {
            currentMeal = meal
            binding.mealName.text = meal.name
            binding.mealSwitch.isChecked = meal.showMeal
        }

        companion object {
            fun create(
                parent: ViewGroup,
                updateMealVisibility: (Long, Boolean) -> Unit,
                navigateToMealNameDialog: (Long) -> Unit
            ): MealListItemViewHolder {
                return MealListItemViewHolder(
                    MealListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), updateMealVisibility,
                    navigateToMealNameDialog
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListItemViewHolder {
        return MealListItemViewHolder.create(parent, updateMealVisibility, navigateToMealNameDialog)
    }

    override fun onBindViewHolder(holder: MealListItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Meal>() {

            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem == newItem
            }
        }
    }

}