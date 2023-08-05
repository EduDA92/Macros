package com.example.macrostracker.ui.diary.foodList

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.databinding.FragmentMyRecipesBinding
import com.example.macrostracker.ui.diary.foodList.deleteDialogFragments.DeleteRecipeDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    private var _binding: FragmentMyRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val adapter = MyRecipesAdapter({recipeId ->
            DeleteRecipeDialog.newInstance(recipeId).show(childFragmentManager, DeleteRecipeDialog.TAG)
        }){ recipeId, recipeName ->
            val action = FoodListFragmentDirections.actionFoodListFragmentToCreateRecipeEntryFragment(recipeName = recipeName,
                recipeId = recipeId,
                date = arguments?.getString(DATE) ?: "",
                mealId = requireArguments().getLong(MEAL_ID))
            navController.navigate(action)
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.apply {
            recipeListRV.layoutManager = LinearLayoutManager(this@MyRecipesFragment.context)
            recipeListRV.adapter = adapter
            recipeListRV.addItemDecoration(
                DividerItemDecoration(
                    this@MyRecipesFragment.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeWithFood.collect { list ->
                        adapter.submitList(list)
                }
            }
        }

        binding.createRecipeButton.setOnClickListener {
            val action = FoodListFragmentDirections.actionFoodListFragmentToCreateRecipe()
            navController.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEAL_ID = "meal_id"
        private const val DATE = "date"

        fun newInstance(mealId: Long, date: String) =
            MyRecipesFragment().apply {
                arguments = Bundle(2).apply {
                    putLong(MEAL_ID, mealId)
                    putString(DATE, date)
                }
            }
    }
}