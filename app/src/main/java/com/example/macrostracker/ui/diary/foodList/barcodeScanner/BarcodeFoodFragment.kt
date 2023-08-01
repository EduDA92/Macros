package com.example.macrostracker.ui.diary.foodList.barcodeScanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.macrostracker.R
import com.example.macrostracker.data.util.ApiResult
import com.example.macrostracker.databinding.FragmentBarcodeFoodBinding
import com.example.macrostracker.databinding.FragmentCreateFoodBinding
import com.example.macrostracker.model.Food
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val FOODNAME = "foodName"
private const val BRAND = "brand"
private const val CALORIES = "Calories"
private const val CARBS = "Carbs"
private const val PROTEIN = "Protein"
private const val FAT = "Fat"
private const val SERVINGSIZE = "ServingSize"

@AndroidEntryPoint
class BarcodeFoodFragment : Fragment() {

    private var _binding: FragmentBarcodeFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarcodeFoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarcodeFoodBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.barcodeFood.collect { result ->
                    when (result) {
                        is BarcodeFoodUiModel.Error -> {
                            /* Hide Loading bar */
                            binding.loadingProgress.visibility = View.GONE

                            /* Show error */
                            binding.errorMessage.visibility = View.VISIBLE
                            binding.errorMessage.text = result.error
                        }

                        is BarcodeFoodUiModel.Loading -> {
                            binding.loadingProgress.visibility = View.VISIBLE
                        }

                        is BarcodeFoodUiModel.Success -> {
                            /* Disable loading and show textViews */
                            binding.loadingProgress.visibility = View.GONE
                            showAllViews()

                            if (savedInstanceState != null) {
                                binding.foodNameTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        FOODNAME
                                    )
                                )
                                binding.ServingSizeTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        SERVINGSIZE
                                    )
                                )
                                binding.brandTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        BRAND
                                    )
                                )
                                binding.CaloriesTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        CALORIES
                                    )
                                )
                                binding.FatTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        FAT
                                    )
                                )
                                binding.ProteinTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        PROTEIN
                                    )
                                )
                                binding.CarbsTextField.editText?.setText(
                                    savedInstanceState.getString(
                                        CARBS
                                    )
                                )
                            } else {
                                result.food?.let { food ->
                                    binding.foodNameTextField.editText?.setText(food.name)
                                    binding.ServingSizeTextField.editText?.setText(food.servingSize.toString())
                                    binding.brandTextField.editText?.setText(food.brand)
                                    binding.CaloriesTextField.editText?.setText(food.calories.toString())
                                    binding.FatTextField.editText?.setText(food.fat.toString())
                                    binding.ProteinTextField.editText?.setText(food.protein.toString())
                                    binding.CarbsTextField.editText?.setText(food.carbs.toString())
                                }

                            }

                        }
                    }
                }
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addFood -> {
                    if (binding.foodNameTextField.editText?.text?.isEmpty() == true ||
                        binding.ServingSizeTextField.editText?.text?.isEmpty() == true ||
                        binding.brandTextField.editText?.text?.isEmpty() == true ||
                        binding.CaloriesTextField.editText?.text?.isEmpty() == true ||
                        binding.ProteinTextField.editText?.text?.isEmpty() == true ||
                        binding.CarbsTextField.editText?.text?.isEmpty() == true ||
                        binding.FatTextField.editText?.text?.isEmpty() == true
                    ) {
                        Snackbar.make(binding.root, "Required field empty", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.insertFood(
                            Food(
                                name = binding.foodNameTextField.editText!!.text.toString(),
                                brand = binding.brandTextField.editText!!.text.toString(),
                                calories = binding.CaloriesTextField.editText!!.text.toString()
                                    .toInt(),
                                carbs = binding.CarbsTextField.editText!!.text.toString().toInt(),
                                fat = binding.FatTextField.editText!!.text.toString().toInt(),
                                protein = binding.ProteinTextField.editText!!.text.toString()
                                    .toInt(),
                                servingSize = binding.ServingSizeTextField.editText!!.text.toString()
                                    .toInt()

                            )
                        )
                        navController.popBackStack(R.id.foodListFragment, false)
                    }
                    true
                }

                else -> false
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FOODNAME, binding.foodNameTextField.editText?.text?.toString())
        outState.putString(SERVINGSIZE, binding.ServingSizeTextField.editText?.text?.toString())
        outState.putString(BRAND, binding.brandTextField.editText?.text?.toString())
        outState.putString(CALORIES, binding.CaloriesTextField.editText?.text?.toString())
        outState.putString(FAT, binding.FatTextField.editText?.text?.toString())
        outState.putString(PROTEIN, binding.ProteinTextField.editText?.text?.toString())
        outState.putString(CARBS, binding.CarbsTextField.editText?.text?.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showAllViews() {
        binding.foodNameTextField.visibility = View.VISIBLE
        binding.ServingSizeTextField.visibility = View.VISIBLE
        binding.brandTextField.visibility = View.VISIBLE
        binding.CaloriesTextField.visibility = View.VISIBLE
        binding.FatTextField.visibility = View.VISIBLE
        binding.ProteinTextField.visibility = View.VISIBLE
        binding.CarbsTextField.visibility = View.VISIBLE
    }
}