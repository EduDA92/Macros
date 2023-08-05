package com.example.macrostracker.ui.diary

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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentDiaryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.user, R.id.diary
            )
        )
        binding.diaryTopAppBar.setupWithNavController(navController, appBarConfiguration)

        /* Setting up the adapter */
        val adapter = DiaryAdapter({ entryId ->
            DeleteEntryDialogFragment.newInstance(entryId)
                .show(childFragmentManager, DeleteEntryDialogFragment.TAG)
        }, { mealId, mealName ->
            val action = DiaryFragmentDirections.actionDiaryFragmentToFoodListFragment(
                mealId = mealId,
                date = viewModel.date.value.toString(),
                mealName = mealName
            )
            navController.navigate(action)
        }, { foodName, foodId, entryId ->
            val action = DiaryFragmentDirections.actionDiaryFragmentToEditEntryFragment(
                foodName = foodName,
                foodId = foodId,
                entryId = entryId
            )
            navController.navigate(action)
        }) {recipeName, recipeId, entryId ->
            val action = DiaryFragmentDirections.actionDiaryFragmentToEditRecipeEntryFragment(
                recipeName = recipeName,
                recipeId = recipeId,
                entryId = entryId
            )
            navController.navigate(action)
        }

        binding.apply {
            diaryPageRecyclerView.layoutManager = LinearLayoutManager(this@DiaryFragment.context)
            diaryPageRecyclerView.adapter = adapter
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        /* Date observer */
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.date.collect { date ->
                        binding.logDateTitle.text = date.toString()
                    }
                }
                launch {
                    viewModel.diaryEntries.collect {
                        it?.let {
                            adapter.submitList(it)
                        }
                    }
                }
            }
        }

        binding.nextDayButton.setOnClickListener {
            viewModel.nextDate()
        }
        binding.prevDayButton.setOnClickListener {
            viewModel.prevDate()
        }

        binding.diaryTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editMealList -> {
                    val action = DiaryFragmentDirections.actionDiaryFragmentToEditMealsFragment()
                    navController.navigate(action)
                    true
                }

                else -> false
            }
        }

    }

    /* Set binding null inside onDestroyView to prevent leaks from the layout when navigating to other
    * fragments, this leak is caused because when navigating from this fragment to another only onDestroyView
    * is called, this means that the Fragment view is destroyed but the fragment itself its not. */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}