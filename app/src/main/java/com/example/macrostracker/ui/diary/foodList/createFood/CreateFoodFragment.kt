package com.example.macrostracker.ui.diary.foodList.createFood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentCreateFoodBinding
import com.example.macrostracker.model.Food
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFoodFragment : Fragment() {

    private var _binding: FragmentCreateFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateFoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

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
                                carbs = binding.CarbsTextField.editText!!.text.toString().toDouble(),
                                fat = binding.FatTextField.editText!!.text.toString().toDouble(),
                                protein = binding.ProteinTextField.editText!!.text.toString()
                                    .toDouble(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}