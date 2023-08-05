package com.example.macrostracker.ui.diary.foodList.createEntry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentEntryBinding
import com.example.macrostracker.model.Entry
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDate

private const val CALORIES = "Calories"
private const val CARBS = "Carbs"
private const val PROTEIN = "Protein"
private const val FAT = "Fat"

@AndroidEntryPoint
class CreateEntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: CreateEntryFragmentArgs by navArgs()

    private val viewModel: CreateEntryViewModel by viewModels()

    private val decimalFormat = DecimalFormat("#.##")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* initialize summary binding */
        if (savedInstanceState != null) {
            binding.caloriesSummary.text = savedInstanceState.getString(CALORIES)
            binding.proteinSummary.text = savedInstanceState.getString(PROTEIN)
            binding.fatSummary.text = savedInstanceState.getString(FAT)
            binding.carbohydrateSummary.text = savedInstanceState.getString(CARBS)
        } else {
            binding.caloriesSummary.text = "0"
            binding.proteinSummary.text = "0"
            binding.fatSummary.text = "0"
            binding.carbohydrateSummary.text = "0"
        }

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)


        /* Logic to add an entry to the diary, if the entry data is correct, insert the entry with the data provided
        * and navigate back to the diary fragment. If the data is incorrect show snackbar and keep the user on the current
        * fragment */
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addEntry -> {
                    if (binding.ServingSizeTextField.editText?.text?.isEmpty() == true) {
                        Snackbar.make(binding.root, "Required field empty", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.insertEntry(
                            Entry(
                                date = LocalDate.parse(navigationArgs.date),
                                mealId = navigationArgs.mealId,
                                foodId = navigationArgs.foodId,
                                recipeId = null,
                                servingSize = binding.ServingSizeTextField.editText!!.text.toString()
                                    .toInt()
                            )
                        )
                        navController.popBackStack(R.id.diaryFragment, false)
                    }
                    true
                }

                else -> false
            }
        }

        /* This scope modify the textViews showing the Macros data when the user changes the servingSize */
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeFood(navigationArgs.foodId).collect { food ->
                    binding.ServingSizeTextField.editText?.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            p0?.let { entryServing ->
                                if (entryServing.isNotEmpty()) {
                                    binding.caloriesSummary.text =
                                        entryServing.toString().toInt().times(food.calories)
                                            .div(food.servingSize).toString()
                                    binding.fatSummary.text = resources.getString(
                                        R.string.formatted_macros_number_double,
                                        decimalFormat.format(
                                            entryServing.toString().toInt().times(food.fat)
                                                .div(food.servingSize)
                                        )
                                    )
                                    binding.carbohydrateSummary.text =
                                        resources.getString(
                                            R.string.formatted_macros_number_double,
                                            decimalFormat.format(
                                                entryServing.toString().toInt().times(food.carbs)
                                                    .div(food.servingSize)
                                            )
                                        )
                                    binding.proteinSummary.text =
                                        resources.getString(
                                            R.string.formatted_macros_number_double,
                                            decimalFormat.format(
                                                entryServing.toString().toInt().times(food.protein)
                                                    .div(food.servingSize)
                                            )
                                        )
                                } else {
                                    binding.caloriesSummary.text = "0"
                                    binding.fatSummary.text = "0"
                                    binding.carbohydrateSummary.text = "0"
                                    binding.proteinSummary.text = "0"
                                }
                            }

                        }

                        override fun afterTextChanged(p0: Editable?) {} })
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CALORIES, binding.caloriesSummary.text.toString())
        outState.putString(PROTEIN, binding.proteinSummary.text.toString())
        outState.putString(CARBS, binding.carbohydrateSummary.text.toString())
        outState.putString(FAT, binding.fatSummary.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}