package com.example.macrostracker.ui.user.inputDialogFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.macrostracker.R
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.databinding.DefaultDropdownInputDialogBinding
import com.example.macrostracker.ui.user.UserViewModel
import kotlinx.coroutines.launch

class SetActivityLevelDialogFragment: DialogFragment() {

    private var _binding: DefaultDropdownInputDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by hiltNavGraphViewModels(R.id.user)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DefaultDropdownInputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.dialogTitle.text = getString(R.string.activity_level_dialog_title)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect { userData ->
                    userData?.let {
                        when(it.activityLevel){
                            ActivityLevelEnum.SEDENTARY -> binding.lastValue.text = getString(R.string.sedentary_dialog_last_value)
                            ActivityLevelEnum.LIGHTLY_ACTIVE -> binding.lastValue.text = getString(R.string.lightly_active_dialog_last_value)
                            ActivityLevelEnum.MODERATELY_ACTIVE -> binding.lastValue.text = getString(R.string.moderately_active_dialog_last_value)
                            ActivityLevelEnum.ACTIVE -> binding.lastValue.text = getString(R.string.active_dialog_last_value)
                            ActivityLevelEnum.VERY_ACTIVE -> binding.lastValue.text = getString(R.string.very_active_dialog_last_value)
                        }
                    }
                }
            }
        }

        binding.setButton.setOnClickListener {
            val activityLevels = resources.getStringArray(R.array.ActivityLevelList).toList()

            if(binding.inputTextField.editText?.text?.isNotEmpty() == true){
                when(binding.inputTextField.editText?.text.toString()){
                    activityLevels[0] -> viewModel.setActivityLevel(ActivityLevelEnum.SEDENTARY)
                    activityLevels[1] -> viewModel.setActivityLevel(ActivityLevelEnum.LIGHTLY_ACTIVE)
                    activityLevels[2] -> viewModel.setActivityLevel(ActivityLevelEnum.MODERATELY_ACTIVE)
                    activityLevels[3] -> viewModel.setActivityLevel(ActivityLevelEnum.ACTIVE)
                    activityLevels[4] -> viewModel.setActivityLevel(ActivityLevelEnum.VERY_ACTIVE)
                }
            }

            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }


    /* This on Resume will fix this: https://github.com/material-components/material-components-android/issues/2012#issuecomment-808853621 */
    override fun onResume() {
        super.onResume()
        val activityLevels = resources.getStringArray(R.array.ActivityLevelList).toList()
        val activityLevelAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, activityLevels)
        binding.dropDown.setAdapter(activityLevelAdapter)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}