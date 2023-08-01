package com.example.macrostracker.ui.diary.editEntry

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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val CALORIES = "Calories"
private const val CARBS = "Carbs"
private const val PROTEIN = "Protein"
private const val FAT = "Fat"
private const val SERVINGSIZE = "ServingSize"

@AndroidEntryPoint
class EditEntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditEntryViewModel by viewModels()

    private val navigationArgs: EditEntryFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)


        /* Logic to update entry, if the entry data is correct, update the entry with the data provided
        * and navigate back to the diary fragment. If the data is incorrect show snackbar and keep the user on the current
        * fragment */
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addEntry -> {
                    if (binding.ServingSizeTextField.editText?.text?.isEmpty() == true) {
                        Snackbar.make(binding.root, "Required field empty", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {

                        viewModel.updateEntryServingSize(
                            navigationArgs.entryId,
                            binding.ServingSizeTextField.editText!!.text.toString().toInt()
                        )
                        navController.navigateUp()
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                /* This scope collects the entry data to show the actual data of the entry */
                launch {
                    viewModel.entryWithFood.collect{observableEntryWithFood ->
                        observableEntryWithFood?.let{entryWithFood ->
                            /* initialize summary binding */
                            if (savedInstanceState != null) {
                                binding.caloriesSummary.text = savedInstanceState.getString(CALORIES)
                                binding.proteinSummary.text = savedInstanceState.getString(PROTEIN)
                                binding.fatSummary.text = savedInstanceState.getString(FAT)
                                binding.carbohydrateSummary.text = savedInstanceState.getString(
                                    CARBS
                                )
                                binding.ServingSizeTextField.editText?.setText(savedInstanceState.getString(
                                    SERVINGSIZE
                                ))
                            } else {
                                binding.caloriesSummary.text = entryWithFood.entryCalories.toString()
                                binding.proteinSummary.text = entryWithFood.entryProtein.toString()
                                binding.fatSummary.text = entryWithFood.entryFat.toString()
                                binding.carbohydrateSummary.text = entryWithFood.entryCarbs.toString()
                                binding.ServingSizeTextField.editText?.setText(entryWithFood.servingSize.toString())
                            }
                        }
                    }
                }

                /* This scope modify the textViews showing the Macros data when the user changes the servingSize */
                launch {
                    viewModel.food.collect { observableFood ->
                        observableFood?.let{food ->
                            binding.ServingSizeTextField.editText?.addTextChangedListener(object :
                                TextWatcher {
                                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                    p0?.let { entryServing ->
                                        if (entryServing.isNotEmpty()) {
                                            binding.caloriesSummary.text =
                                                entryServing.toString().toInt().times(food.calories)
                                                    .div(food.servingSize).toString()
                                            binding.fatSummary.text =
                                                entryServing.toString().toInt().times(food.fat)
                                                    .div(food.servingSize).toString()
                                            binding.carbohydrateSummary.text =
                                                entryServing.toString().toInt().times(food.carbs)
                                                    .div(food.servingSize).toString()
                                            binding.proteinSummary.text =
                                                entryServing.toString().toInt().times(food.protein)
                                                    .div(food.servingSize).toString()
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
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CALORIES, binding.caloriesSummary.text.toString())
        outState.putString(PROTEIN, binding.proteinSummary.text.toString())
        outState.putString(CARBS, binding.carbohydrateSummary.text.toString())
        outState.putString(FAT, binding.fatSummary.text.toString())
        outState.putString(SERVINGSIZE, binding.ServingSizeTextField.editText?.text?.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}