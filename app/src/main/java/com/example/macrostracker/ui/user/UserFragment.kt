package com.example.macrostracker.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.macrostracker.R
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.data.util.enums.WeightEnum
import com.example.macrostracker.databinding.FragmentUserBinding
import com.example.macrostracker.util.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by hiltNavGraphViewModels(R.id.user)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.user, R.id.diary
            )
        )

        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        val decimalFormat = DecimalFormat("#.##")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect {
                    it?.let { userData ->
                        /* If its the first time opening the app first time value will be false,
                        * do whatever first time things here.*/

                        if (!userData.firstTime) {
                            /* noLongerFirstTime function MUST be first, if not, as the data changes the flow will get
                            * triggered multiple times and the populateMeals function will be triggered more than once and
                            * the database will be populated with duplicated meals */
                            viewModel.noLongerFirstTime()
                            /* Set default values to the user data */
                            viewModel.setWeight(60.0)
                            viewModel.setHeight(1.60)
                            viewModel.setAge(20)

                            viewModel.populateMeals()
                        }

                        /* Age */
                        binding.ageTextNumber.text = userData.age.toString()
                        binding.ageTextNumber.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetAgeDialogFragment()
                            navController.navigate(action)
                        }

                        /* Height */
                        binding.heightTextNumber.text = decimalFormat.format(userData.height)
                        binding.heightTextNumber.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetHeightDialogFragment()
                            navController.navigate(action)
                        }

                        /* Weight */
                        binding.weightTextNumber.text = decimalFormat.format(userData.weight)
                        binding.weightTextNumber.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetWeightDialogFragment()
                            navController.navigate(action)
                        }

                        /* Gender */
                        binding.setGenderText.text = when (userData.gender) {
                            GenderEnum.MALE -> getString(R.string.genderMale)
                            GenderEnum.FEMALE -> getString(R.string.genderFemale)
                        }
                        binding.setGenderText.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetGenderDialogFragment()
                            navController.navigate(action)
                        }


                        binding.heightText.text = when (userData.heightUnits) {
                            HeightEnum.M -> getString(R.string.heightInM)
                            HeightEnum.FT -> getString(R.string.heightInFt)
                        }

                        binding.weightText.text = when (userData.weightUnits) {
                            WeightEnum.KG -> getString(R.string.weightInKg)
                            WeightEnum.LB -> getString(R.string.weightInLb)
                        }

                        /* Activity level */
                        binding.setActivityLevelText.text = when (userData.activityLevel) {
                            ActivityLevelEnum.SEDENTARY -> getString(R.string.sedentary)
                            ActivityLevelEnum.LIGHTLY_ACTIVE -> getString(R.string.lightly_active)
                            ActivityLevelEnum.MODERATELY_ACTIVE -> getString(R.string.moderately_active)
                            ActivityLevelEnum.ACTIVE -> getString(R.string.active)
                            ActivityLevelEnum.VERY_ACTIVE -> getString(R.string.very_active)
                        }
                        binding.setActivityLevelText.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetActivityLevelDialogFragment()
                            navController.navigate(action)
                        }

                        /* Objective level */
                        binding.setObjectiveLevelText.text = when (userData.objective) {
                            ObjectiveEnum.LOSE_WEIGHT -> getString(R.string.lose_weight)
                            ObjectiveEnum.MAINTAIN_WEIGHT -> getString(R.string.maintain_weight)
                            ObjectiveEnum.GAIN_WEIGHT -> getString(R.string.gain_weight)
                        }
                        binding.setObjectiveLevelText.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetObjectiveDialogFragment()
                            navController.navigate(action)
                        }

                        binding.setBmrText.text = viewModel.calculateBMR(userData).toString()
                        binding.setImcText.text =
                            decimalFormat.format(viewModel.calculateIMC(userData))

                        binding.editButton.setOnSafeClickListener {
                            val action =
                                UserFragmentDirections.actionUserFragmentToSetUnitsDialogFragment()
                            navController.navigate(action)
                        }
                    }
                }
            }
        }

        binding.userFoodList.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToUserFoodListFragment()
            navController.navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}