package com.example.macrostracker.ui.diary.foodList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.LifecycleCameraController
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macrostracker.R
import com.example.macrostracker.databinding.FragmentMyFoodsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyFoodsFragment : Fragment() {

    private var _binding: FragmentMyFoodsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navController = findNavController()

        /* Setup RecyclerView */
        val adapter = MyFoodsAdapter { foodId, foodName ->
            val action =
                FoodListFragmentDirections.actionFoodListFragmentToEntryFragment(
                    requireArguments().getLong(MEAL_ID), foodId, arguments?.getString(DATE) ?: "", foodName
                )
            navController.navigate(action)
        }
        binding.apply {
            foodListRV.layoutManager = LinearLayoutManager(this@MyFoodsFragment.context)
            foodListRV.adapter = adapter
            foodListRV.addItemDecoration(
                DividerItemDecoration(
                    this@MyFoodsFragment.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.createFoodButton.setOnClickListener {
            val action = FoodListFragmentDirections.actionFoodListFragmentToCreateFoodFragment()
            navController.navigate(action)
        }
        binding.scanFoodButton.setOnClickListener {
            val action = FoodListFragmentDirections.actionFoodListFragmentToBarcodeScannerFragment()
            navController.navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.foodList.collect {
                    adapter.submitList(it)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEAL_ID = "meal_id"
        private const val DATE = "date"

        fun newInstance(mealId: Long, date: String) =
            MyFoodsFragment().apply {
                arguments = Bundle(2).apply {
                    putLong(MEAL_ID, mealId)
                    putString(DATE, date)
                }
            }
    }
}