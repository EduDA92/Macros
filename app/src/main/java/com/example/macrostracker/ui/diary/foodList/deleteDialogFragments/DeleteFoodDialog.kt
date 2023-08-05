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

class DeleteFoodDialog: DialogFragment() {

    private var _binding: DefaultDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodListViewModel by viewModels(ownerProducer = {requireParentFragment()})

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

        binding.dialogTitle.text = getString(R.string.deleteFoodDialogTitle)
        binding.deleteText.text = getString(R.string.deleteFoodDialogText)

        binding.deleteButton.setOnClickListener {
            arguments?.getLong(FOOD_ID)?.let{id ->
                viewModel.deleteFood(id)
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        const val TAG = "DeleteFoodDialog"
        private const val FOOD_ID = "food_id"

        fun newInstance(foodId: Long) =
            DeleteFoodDialog().apply {
                arguments = Bundle(2).apply {
                    putLong(FOOD_ID, foodId)
                }
            }
    }
}