package com.example.macrostracker.ui.diary.foodList.createRecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentCreateRecipeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateRecipeFragment : Fragment() {

    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by hiltNavGraphViewModels(R.id.createRecipe)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateRecipeBinding.inflate(layoutInflater, container, false)
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
                    if (binding.recipeNameTextField.editText?.text?.isEmpty() == true ||
                        binding.recipeDescriptionTextField.editText?.text?.isEmpty() == true
                    ) {

                        Snackbar.make(binding.root, "Required field empty", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {

                        viewLifecycleOwner.lifecycleScope.launch {

                            val recipeId = viewModel.insertRecipe(
                                binding.recipeNameTextField.editText!!.text.toString(),
                                binding.recipeDescriptionTextField.editText!!.text.toString()
                            )


                            val action = CreateRecipeFragmentDirections.actionCreateRecipeFragmentToCreateRecipeIngredientsFragment(
                                recipeName = binding.recipeNameTextField.editText!!.text.toString(),
                                recipeId = recipeId
                            )

                            navController.navigate(action)
                        }


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