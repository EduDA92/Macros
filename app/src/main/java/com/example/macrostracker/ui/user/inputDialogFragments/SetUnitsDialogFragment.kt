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
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.WeightEnum
import com.example.macrostracker.databinding.UnitsInputDialogBinding
import com.example.macrostracker.ui.user.UserViewModel
import kotlinx.coroutines.launch

class SetUnitsDialogFragment : DialogFragment() {
    private var _binding: UnitsInputDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by hiltNavGraphViewModels(R.id.user)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UnitsInputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.dialogTitle.text = getString(R.string.units_dialog_title)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect { userData ->
                    userData?.let {
                        when (it.heightUnits) {
                            HeightEnum.M -> binding.lastValueHeightUnit.text =
                                getString(R.string.meter_dialog_last_value)

                            HeightEnum.FT -> binding.lastValueHeightUnit.text =
                                getString(R.string.foot_dialog_last_value)
                        }
                        when (it.weightUnits) {
                            WeightEnum.KG -> binding.lastValueWeightUnit.text =
                                getString(R.string.kilogram_dialog_last_value)

                            WeightEnum.LB -> binding.lastValueWeightUnit.text =
                                getString(R.string.pounds_dialog_last_value)
                        }
                    }
                }
            }
        }

        binding.setButton.setOnClickListener {
            val weightUnits = resources.getStringArray(R.array.weightUnitList)
            val heightUnits = resources.getStringArray(R.array.heightUnitList)

            if(binding.inputWeightUnitTextField.editText?.text?.isNotEmpty() == true){
                when (binding.inputWeightUnitTextField.editText?.text.toString()) {
                    weightUnits[0] -> viewModel.setWeightUnits(WeightEnum.KG)
                    weightUnits[1] -> viewModel.setWeightUnits(WeightEnum.LB)
                }
            }
            if(binding.inputHeightUnitTextField.editText?.text?.isNotEmpty() == true){
                when (binding.inputHeightUnitTextField.editText?.text.toString()) {
                    heightUnits[0] -> viewModel.setHeightUnits(HeightEnum.M)
                    heightUnits[1] -> viewModel.setHeightUnits(HeightEnum.FT)
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
        val weightUnits = resources.getStringArray(R.array.weightUnitList)
        val weightUnitsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, weightUnits)
        binding.weightDropDown.setAdapter(weightUnitsAdapter)

        val heightUnits = resources.getStringArray(R.array.heightUnitList)
        val heightUnitsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, heightUnits)
        binding.heightDropDown.setAdapter(heightUnitsAdapter)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}