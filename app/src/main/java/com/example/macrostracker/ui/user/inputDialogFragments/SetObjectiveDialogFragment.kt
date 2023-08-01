package com.example.macrostracker.ui.user.inputDialogFragments

import android.os.Bundle
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
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.databinding.DefaultDropdownInputDialogBinding
import com.example.macrostracker.ui.user.UserViewModel
import kotlinx.coroutines.launch

class SetObjectiveDialogFragment : DialogFragment() {
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

        binding.dialogTitle.text = getString(R.string.objective_dialog_title)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect { userData ->
                    userData?.let {
                        when (it.objective) {
                            ObjectiveEnum.LOSE_WEIGHT -> binding.lastValue.text =
                                getString(R.string.lose_weight_dialog_last_value)

                            ObjectiveEnum.MAINTAIN_WEIGHT -> binding.lastValue.text =
                                getString(R.string.maintain_weight_dialog_last_value)

                            ObjectiveEnum.GAIN_WEIGHT -> binding.lastValue.text =
                                getString(R.string.gain_weight_dialog_last_value)
                        }
                    }
                }
            }
        }

        binding.setButton.setOnClickListener {
            val objectives = resources.getStringArray(R.array.objectiveList).toList()

            if (binding.inputTextField.editText?.text?.isNotEmpty() == true) {
                when (binding.inputTextField.editText?.text.toString()) {
                    objectives[0] -> viewModel.setObjective(ObjectiveEnum.LOSE_WEIGHT)
                    objectives[1] -> viewModel.setObjective(ObjectiveEnum.MAINTAIN_WEIGHT)
                    objectives[2] -> viewModel.setObjective(ObjectiveEnum.GAIN_WEIGHT)
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
        val objectives = resources.getStringArray(R.array.objectiveList).toList()
        val objectiveAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, objectives)
        binding.dropDown.setAdapter(objectiveAdapter)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}