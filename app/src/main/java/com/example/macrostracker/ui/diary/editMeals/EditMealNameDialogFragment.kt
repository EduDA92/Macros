package com.example.macrostracker.ui.diary.editMeals

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.example.macrostracker.R
import com.example.macrostracker.databinding.DefaultInputDialogBinding

class EditMealNameDialogFragment: DialogFragment() {

    private var _binding: DefaultInputDialogBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditMealNameDialogFragmentArgs by navArgs()

    private val viewModel: EditMealsViewModel by hiltNavGraphViewModels(R.id.editMeals)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DefaultInputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )


        binding.dialogTitle.text = getString(R.string.edit_meal_name)
        binding.inputTextFieldEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.lastValue.isVisible = false

        binding.setButton.setOnClickListener {
            if (binding.inputTextField.editText?.text?.isNotEmpty() == true) {
                viewModel.updateMealName(navigationArgs.mealId, binding.inputTextField.editText!!.text.toString())
            }
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