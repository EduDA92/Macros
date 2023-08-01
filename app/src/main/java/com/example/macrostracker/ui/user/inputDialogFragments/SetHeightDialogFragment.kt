package com.example.macrostracker.ui.user.inputDialogFragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.macrostracker.R
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.databinding.DefaultInputDialogBinding
import com.example.macrostracker.ui.user.UserViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class SetHeightDialogFragment : DialogFragment() {

    private var _binding: DefaultInputDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by hiltNavGraphViewModels(R.id.user)

    private val decimalFormat = DecimalFormat("#.##")

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

        binding.dialogTitle.text = getString(R.string.height_dialog_title)
        binding.inputTextFieldEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect { userData ->
                    userData?.let {
                        when(it.heightUnits){
                            HeightEnum.M -> binding.lastValue.text = getString(R.string.height_dialog_last_value_m, decimalFormat.format(it.height))
                            HeightEnum.FT -> binding.lastValue.text = getString(R.string.height_dialog_last_value_ft, decimalFormat.format(it.height))
                        }
                    }
                }
            }
        }

        binding.setButton.setOnClickListener {
            if (binding.inputTextField.editText?.text?.isNotEmpty() == true) {
                viewModel.setHeight(binding.inputTextField.editText!!.text.toString().toDouble())
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