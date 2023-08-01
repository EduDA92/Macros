package com.example.macrostracker.ui.diary.editMeals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentEditMealsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditMealsFragment : Fragment() {

    private var _binding: FragmentEditMealsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditMealsViewModel by hiltNavGraphViewModels(R.id.editMeals)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        /* setting up recyclerview */
        val adapter = MealListAdapter({ mealId, showMeal ->
            viewModel.updateMealVisibility(mealId, showMeal)
        }
        ) {mealId ->
            val action = EditMealsFragmentDirections.actionEditMealsFragmentToEditMealNameDialogFragment3(mealId)
            navController.navigate(action)
        }
        binding.apply {
            mealList.layoutManager = LinearLayoutManager(this@EditMealsFragment.context)
            mealList.adapter = adapter
            mealList.setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mealList.collect { list ->
                    if (list.isNotEmpty()) {
                        adapter.submitList(list)
                    }
                }
            }
        }

    }


}