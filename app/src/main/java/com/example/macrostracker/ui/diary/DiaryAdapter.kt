package com.example.macrostracker.ui.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.macrostracker.R
import com.example.macrostracker.model.EntryWithFood
import com.example.macrostracker.model.Meal
import com.example.macrostracker.databinding.DiaryAddFoodItemBinding
import com.example.macrostracker.databinding.DiaryEntryItemBinding
import com.example.macrostracker.databinding.DiaryEntrySummaryItemBinding
import com.example.macrostracker.databinding.DiaryEntryTitleItemBinding
import com.example.macrostracker.databinding.DiaryEntryTotalSummaryItemBinding

class DiaryAdapter(
    private val deleteEntry: (Long) -> Unit,
    private val navigate: (Long, String) -> Unit,
    private val editEntry: (String, Long, Long) -> Unit
) : ListAdapter<UiModel, ViewHolder>(DiffCallBack) {

    class DiaryEntryTitleViewHolder(
        private var binding: DiaryEntryTitleItemBinding,
    ) : ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.entryItemTitle.text = meal.name
        }

        /* Info about this way of creating the ViewHolder:
        * https://kotlinlang.org/docs/object-declarations.html#companion-objects
        * https://blog.mindorks.com/companion-object-in-kotlin/ */

        companion object {
            fun create(parent: ViewGroup): DiaryEntryTitleViewHolder {
                return DiaryEntryTitleViewHolder(
                    DiaryEntryTitleItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    class DiaryEntryItemViewHolder(
        private var binding: DiaryEntryItemBinding,
        private val deleteEntry: (Long) -> Unit,
        private val editEntry: (String, Long, Long) -> Unit
    ) : ViewHolder(binding.root) {

        private var currentEntry: EntryWithFood? = null

        init {
            itemView.setOnLongClickListener {
                currentEntry?.let {
                    deleteEntry(it.id)
                }
                true
            }

            itemView.setOnClickListener {
                currentEntry?.let {
                    editEntry(it.food.name, it.food.id, it.id)
                }
            }
        }

        fun bind(entry: EntryWithFood) {
            val resources = itemView.resources
            currentEntry = entry
            binding.foodNameAndDescription.text = resources.getString(
                R.string.food_name_brand,
                entry.food.name,
                entry.food.brand
            )
            binding.entryServing.text =
                resources.getString(R.string.formatted_macros_number, entry.servingSize)
            binding.entryMacros.text = resources.getString(
                R.string.formatted_entry_macros,
                entry.entryCalories,
                entry.entryProtein,
                entry.entryFat,
                entry.entryCarbs
            )

        }

        companion object {
            fun create(
                parent: ViewGroup,
                deleteEntry: (Long) -> Unit,
                editEntry: (String, Long, Long) -> Unit
            ): DiaryEntryItemViewHolder {
                return DiaryEntryItemViewHolder(
                    DiaryEntryItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), deleteEntry, editEntry
                )
            }
        }

    }

    class DiaryAddFoodViewHolder(
        private var binding: DiaryAddFoodItemBinding,
        private val navigate: (Long, String) -> Unit
    ) : ViewHolder(binding.root) {

        private var currentMeal: Meal? = null

        init {
            binding.addFoodButton.setOnClickListener {
                currentMeal?.let {
                    navigate(it.id, it.name)
                }
            }

        }

        fun bind(meal: Meal) {
            currentMeal = meal
        }

        companion object {
            fun create(
                parent: ViewGroup,
                navigate: (Long, String) -> Unit
            ): DiaryAddFoodViewHolder {
                return DiaryAddFoodViewHolder(
                    DiaryAddFoodItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), navigate
                )


            }
        }

    }

    class DiaryEntrySummaryViewHolder(
        private var binding: DiaryEntrySummaryItemBinding
    ) : ViewHolder(binding.root) {


        fun bind(macros: MacrosSummary) {
            val resources = itemView.resources

            binding.caloriesSummary.text = macros.calories.toString()
            binding.fatSummary.text =
                resources.getString(R.string.formatted_macros_number, macros.fat)
            binding.carbohydrateSummary.text =
                resources.getString(R.string.formatted_macros_number, macros.carbohydrate)
            binding.proteinSummary.text =
                resources.getString(R.string.formatted_macros_number, macros.protein)
        }

        companion object {
            fun create(parent: ViewGroup): DiaryEntrySummaryViewHolder {
                return DiaryEntrySummaryViewHolder(
                    DiaryEntrySummaryItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    class DiaryEntryTotalSummaryViewHolder(
        private var binding: DiaryEntryTotalSummaryItemBinding
    ) : ViewHolder(binding.root) {


        fun bind(totalMacros: MacrosSummary, referenceMacros: MacrosSummary) {
            val resources = itemView.resources

            binding.title.text = resources.getString(R.string.total_macros_summary_title)
            binding.caloriesSummary.text = resources.getString(
                R.string.formatted_total_macros_summary_calories,
                totalMacros.calories,
                referenceMacros.calories
            )
            binding.fatSummary.text = resources.getString(
                R.string.formatted_total_macros_summary,
                totalMacros.fat,
                referenceMacros.fat
            )
            binding.carbohydrateSummary.text = resources.getString(
                R.string.formatted_total_macros_summary,
                totalMacros.carbohydrate,
                referenceMacros.carbohydrate
            )
            binding.proteinSummary.text = resources.getString(
                R.string.formatted_total_macros_summary,
                totalMacros.protein,
                referenceMacros.protein
            )
        }

        companion object {
            fun create(parent: ViewGroup): DiaryEntryTotalSummaryViewHolder {
                return DiaryEntryTotalSummaryViewHolder(
                    DiaryEntryTotalSummaryItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.diary_add_food_item -> {
                DiaryAddFoodViewHolder.create(parent, navigate)
            }

            R.layout.diary_entry_item -> {
                DiaryEntryItemViewHolder.create(parent, deleteEntry, editEntry)
            }

            R.layout.diary_entry_summary_item -> {
                DiaryEntrySummaryViewHolder.create(parent)
            }

            R.layout.diary_entry_title_item -> {
                DiaryEntryTitleViewHolder.create(parent)
            }

            R.layout.diary_entry_total_summary_item -> {
                DiaryEntryTotalSummaryViewHolder.create(parent)
            }

            else -> {
                throw UnsupportedOperationException("Unknown view")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is UiModel.AddFoodItem -> (holder as DiaryAddFoodViewHolder).bind(uiModel.meal)
            is UiModel.EntryItem -> (holder as DiaryEntryItemViewHolder).bind(uiModel.entry)
            is UiModel.EntrySummaryItem -> (holder as DiaryEntrySummaryViewHolder).bind(uiModel.macros)
            is UiModel.TitleItem -> (holder as DiaryEntryTitleViewHolder).bind(uiModel.meal)
            is UiModel.TotalSummaryItem -> (holder as DiaryEntryTotalSummaryViewHolder).bind(
                uiModel.totalMacros,
                uiModel.referenceMacros
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.AddFoodItem -> R.layout.diary_add_food_item
            is UiModel.EntryItem -> R.layout.diary_entry_item
            is UiModel.EntrySummaryItem -> R.layout.diary_entry_summary_item
            is UiModel.TitleItem -> R.layout.diary_entry_title_item
            is UiModel.TotalSummaryItem -> R.layout.diary_entry_total_summary_item
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<UiModel>() {

            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.EntryItem && newItem is UiModel.EntryItem && oldItem.entry.id == newItem.entry.id) ||
                        (oldItem is UiModel.TitleItem && newItem is UiModel.TitleItem && oldItem.meal.id == newItem.meal.id) ||
                        (oldItem is UiModel.EntrySummaryItem && newItem is UiModel.EntrySummaryItem && oldItem == newItem) ||
                        (oldItem is UiModel.AddFoodItem && newItem is UiModel.AddFoodItem && oldItem.meal.id == newItem.meal.id) ||
                        (oldItem is UiModel.TotalSummaryItem && newItem is UiModel.TotalSummaryItem && oldItem == newItem)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}
