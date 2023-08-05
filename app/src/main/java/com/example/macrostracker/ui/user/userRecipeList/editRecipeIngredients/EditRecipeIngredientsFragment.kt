package com.example.macrostracker.ui.user.userRecipeList.editRecipeIngredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentCreateRecipeIngredientsBinding
import com.example.macrostracker.ui.diary.foodList.createRecipe.addRecipeIngredients.RecipeIngredientsAdapter
import com.example.macrostracker.ui.user.userRecipeList.UserRecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/* This Fragment is reusing all of the code/classes created for creating a Recipe diary.foodList.createRecipe */
@AndroidEntryPoint
class EditRecipeIngredientsFragment: Fragment() {

    private var _binding: FragmentCreateRecipeIngredientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserRecipeListViewModel by hiltNavGraphViewModels(R.id.userRecipeList)

    private val navigationArgs: EditRecipeIngredientsFragmentArgs by navArgs()

    private val decimalFormat = DecimalFormat("#.##")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateRecipeIngredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        val adapter = RecipeIngredientsAdapter(){foodId ->
            val action = EditRecipeIngredientsFragmentDirections.actionEditRecipeIngredientsFragmentToUserDeleteRecipeIngredient(
                foodId = foodId,
                recipeId = navigationArgs.recipeId
            )
            navController.navigate(action)
        }

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.apply {
            recipeIngredientList.layoutManager =
                LinearLayoutManager(this@EditRecipeIngredientsFragment.context)
            recipeIngredientList.adapter = adapter
            recipeIngredientList.addItemDecoration(
                DividerItemDecoration(
                    this@EditRecipeIngredientsFragment.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getRecipeFromId(navigationArgs.recipeId).collect {
                    it?.let { recipeWithFood ->
                        /* Macros Summary data */
                        binding.servingSizeSummary.text = getString(
                            R.string.serving_size_string_resource,
                            recipeWithFood.servingSize
                        )
                        binding.caloriesSummary.text = recipeWithFood.calories.toString()
                        binding.proteinSummary.text = decimalFormat.format(recipeWithFood.protein)
                        binding.carbohydrateSummary.text =
                            decimalFormat.format(recipeWithFood.carbs)
                        binding.fatSummary.text = decimalFormat.format(recipeWithFood.fat)

                        adapter.submitList(viewModel.formatToFoodWithQuantity(recipeWithFood))
                    }
                }
            }
        }

        binding.addIngredientButton.setOnClickListener {
            val action = EditRecipeIngredientsFragmentDirections.actionEditRecipeIngredientsFragmentToUserRecipeIngredientListFragment(
                recipeName = navigationArgs.recipeName,
                recipeId = navigationArgs.recipeId
            )
            navController.navigate(action)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.addFood -> {
                    navController.navigateUp()
                    true
                } else -> false
            }
        }
    }
}