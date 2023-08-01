package com.example.macrostracker.ui.user.userFoodList.editFood

import android.os.Bundle
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
class EditFoodFragment : Fragment() {

    private var _binding: FragmentCreateFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditFoodViewModel by viewModels()

    private val navigationArgs: EditFoodFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateFoodBinding.inflate(layoutInflater, container, false)
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
                viewModel.food.collect {
                    it?.let { food ->

                        /* If there is SaveInstanceState values(ex. device rotation) use the saved values
                        * else use ROOM db values */

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
                            binding.FatTextField.editText?.setText(savedInstanceState.getString(FAT))
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
                        viewModel.updateFood(
                            Food(
                                id = navigationArgs.foodId,
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
                        navController.navigateUp()
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
}