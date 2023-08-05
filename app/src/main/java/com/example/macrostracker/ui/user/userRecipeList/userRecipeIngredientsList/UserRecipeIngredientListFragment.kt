package com.example.macrostracker.ui.user.userRecipeList.userRecipeIngredientsList

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
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentIngredientListBinding
import com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList.IngredientDetailsLookup
import com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList.IngredientKeyProvider
import com.example.macrostracker.ui.diary.foodList.createRecipe.ingredientList.IngredientListAdapter
import com.example.macrostracker.ui.user.userRecipeList.UserRecipeListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/* This Fragment is reusing all of the code/classes created for creating a Recipe diary.foodList.createRecipe */
private const val BUTTONENABLED = "buttonEnabled"
@AndroidEntryPoint
class UserRecipeIngredientListFragment : Fragment() {

    private var _binding: FragmentIngredientListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserRecipeListViewModel by hiltNavGraphViewModels(R.id.userRecipeList)
    private val navigationArgs: UserRecipeIngredientListFragmentArgs by navArgs()

    private lateinit var tracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null

    /* Creating the callback that will handle the actions of the CAB */
    val callback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val menuInflater = mode?.menuInflater
            menuInflater?.inflate(R.menu.add_food_top_app_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.addFood -> {
                    /* get the selected food Id*/
                    val foodId = tracker.selection.toList()[0]

                    if (binding.ingredientQuantityTextField.editText?.text?.isNotEmpty() == true && tracker.hasSelection()) {
                        viewModel.createFoodRecipeCrossRef(
                            recipeId = navigationArgs.recipeId,
                            foodId = foodId,
                            quantity = binding.ingredientQuantityTextField.editText?.text.toString()
                                .toInt()
                        )
                        actionMode?.finish()
                        findNavController().navigateUp()
                    } else {
                        Snackbar.make(binding.root, "Required field empty", Snackbar.LENGTH_SHORT)
                            .show()
                    }

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
        _binding = FragmentIngredientListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Restore the state of the quantity texfield */
        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean(BUTTONENABLED)){
                binding.ingredientQuantityTextField.isEnabled = true
            }
        }

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        /* RecyclerView Setup */
        val adapter = IngredientListAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.apply {
            ingredientList.layoutManager = LinearLayoutManager(this@UserRecipeIngredientListFragment.context)
            ingredientList.adapter = adapter
            ingredientList.addItemDecoration(
                DividerItemDecoration(
                    this@UserRecipeIngredientListFragment.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        /* Submit food list to the adapter */
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.foodList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }

        tracker = SelectionTracker.Builder(
            "User-Ingredient-List",
            binding.ingredientList,
            IngredientKeyProvider(adapter),
            IngredientDetailsLookup(binding.ingredientList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectSingleAnything()
        ).build()

        adapter.selectionTracker = tracker

        /* Activate & deactivate action mode and enable disable input for quantity */
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (tracker.hasSelection()) {
                    binding.ingredientQuantityTextField.isEnabled = true
                    if (actionMode == null) {
                        actionMode = requireActivity().startActionMode(callback)
                    }
                    actionMode?.title = "${tracker.selection.size()}"
                } else {
                    binding.ingredientQuantityTextField.isEnabled = false
                    actionMode?.finish()
                }
            }
        })

        /* searchBar */
        binding.searchIngredient.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateFoodSearchText(p0.toString())
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
        outState.putBoolean(BUTTONENABLED, binding.ingredientQuantityTextField.isEnabled)
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