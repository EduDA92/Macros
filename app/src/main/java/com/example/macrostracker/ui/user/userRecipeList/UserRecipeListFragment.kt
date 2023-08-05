package com.example.macrostracker.ui.user.userRecipeList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentUserRecipeListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserRecipeListFragment : Fragment() {

    private var _binding: FragmentUserRecipeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserRecipeListViewModel by hiltNavGraphViewModels(R.id.userRecipeList)

    private lateinit var tracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null

    /* Creating the callback that will handle the actions of the CAB */
    val callback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val menuInflater = mode?.menuInflater
            menuInflater?.inflate(R.menu.contextual_action_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.deleteFood -> {
                    val foodList = tracker.selection.toList()
                    viewModel.deleteRecipeList(foodList)
                    actionMode?.finish()
                    true
                }

                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.clearSelection()
            actionMode = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserRecipeListBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.userFoodListTopAppBar.setupWithNavController(navController, appBarConfiguration)

        /* RV config */
        val adapter = UserRecipeListAdapter(){recipeName, recipeId ->
            val action = UserRecipeListFragmentDirections.actionUserRecipeListFragmentToEditRecipeIngredientsFragment(
                recipeName = recipeName,
                recipeId = recipeId
            )
            navController.navigate(action)
        }

        binding.apply {
            userRecipeListRecyclerView.layoutManager =
                LinearLayoutManager(this@UserRecipeListFragment.context)
            userRecipeListRecyclerView.adapter = adapter
            userRecipeListRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    this@UserRecipeListFragment.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        tracker = SelectionTracker.Builder(
            "Recipe-list",
            binding.userRecipeListRecyclerView,
            RecipeKeyProvider(adapter),
            RecipeDetailsLookup(binding.userRecipeListRecyclerView),
            StorageStrategy.createLongStorage()
        ).build()

        adapter.selectionTracker = tracker

        /* Activate & deactivate action mode */
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>(){
            override fun onSelectionChanged() {
                if(tracker.hasSelection()){
                    if(actionMode == null){
                        actionMode = requireActivity().startActionMode(callback)
                    }
                    actionMode?.title = "${tracker.selection.size()}"
                } else {
                    actionMode?.finish()
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }

        binding.searchRecipe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateSearchText(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        tracker.clearSelection()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::tracker.isInitialized) {
            tracker.onSaveInstanceState(outState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tracker.onRestoreInstanceState(savedInstanceState)

        if (tracker.hasSelection()) {
            actionMode = requireActivity().startActionMode(callback)
            actionMode?.title = "${tracker.selection.size()}"
        }
    }
}