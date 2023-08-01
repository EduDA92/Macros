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
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.databinding.DefaultDropdownInputDialogBinding
import com.example.macrostracker.ui.user.UserViewModel
import kotlinx.coroutines.launch

class SetGenderDialogFragment : DialogFragment() {

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

        binding.dialogTitle.text = getString(R.string.gender_dialog_title)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect { userData ->
                    userData?.let {
                        when (it.gender) {
                            GenderEnum.MALE -> binding.lastValue.text = getString(
                                R.string.gender_male_dialog_last_value,
                                it.gender.toString()
                            )

                            GenderEnum.FEMALE -> binding.lastValue.text = getString(
                                R.string.gender_female_dialog_last_value,
                                it.gender.toString()
                            )
                        }
                    }
                }
            }
        }

        binding.setButton.setOnClickListener {
            val genders = resources.getStringArray(R.array.genderList).toList()

            if (binding.inputTextField.editText?.text?.isNotEmpty() == true) {
                when (binding.inputTextField.editText?.text.toString()) {
                    genders[0] -> viewModel.setGender(GenderEnum.MALE)
                    genders[1] -> viewModel.setGender(GenderEnum.FEMALE)
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
        val genders = resources.getStringArray(R.array.genderList)
        val genderArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.dropDown.setAdapter(genderArrayAdapter)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}