package com.example.macrostracker.ui.diary.foodList.deleteDialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.macrostracker.R
import com.example.macrostracker.databinding.DefaultDeleteDialogBinding
import com.example.macrostracker.ui.diary.foodList.FoodListViewModel

class DeleteRecipeDialog : DialogFragment() {

    private var _binding: DefaultDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DefaultDeleteDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.dialogTitle.text = getString(R.string.deleteRecipeDialogTitle)
        binding.deleteText.text = getString(R.string.deleteRecipeDialogText)

        binding.deleteButton.setOnClickListener {
            arguments?.getLong(RECIPE_ID)?.let { id ->
                viewModel.deleteRecipe(id)
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        const val TAG = "DeleteRecipeDialog"
        private const val RECIPE_ID = "recipe_id"

        fun newInstance(recipeId: Long) =
            DeleteRecipeDialog().apply {
                arguments = Bundle(2).apply {
                    putLong(RECIPE_ID, recipeId)
                }
            }
    }
}