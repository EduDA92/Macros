package com.example.macrostracker.ui.diary.foodList.createRecipe.addRecipeIngredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.example.macrostracker.R
import com.example.macrostracker.databinding.DefaultDeleteDialogBinding
import com.example.macrostracker.ui.diary.foodList.createRecipe.RecipeViewModel

class DeleteIngredientDialogFragment : DialogFragment() {

    private var _binding: DefaultDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by hiltNavGraphViewModels(R.id.createRecipe)

    private val navigationArgs: DeleteIngredientDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DefaultDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.dialogTitle.text = getString(R.string.deleteDialogTitle)
        binding.deleteText.text = getString(R.string.deleteDialogText)

        binding.deleteButton.setOnClickListener {
            viewModel.deleteIngredient(
                recipeId = navigationArgs.recipeId,
                foodId = navigationArgs.foodId
            )
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}