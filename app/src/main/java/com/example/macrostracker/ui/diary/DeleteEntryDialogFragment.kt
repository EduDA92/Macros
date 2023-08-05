package com.example.macrostracker.ui.diary

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.macrostracker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteEntryDialogFragment : DialogFragment() {

    private val viewModel: DiaryViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_entry_title))
            .setMessage(getString(R.string.delete_entry))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                arguments?.getLong(ID)?.let {
                    viewModel.deleteEntry(it)
                    dismiss()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .create()

    companion object {
        const val TAG = "DeleteEntryDialog"
        const val ID = "entryId"

        fun newInstance(foodId: Long) = DeleteEntryDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(ID, foodId)
            }
        }
    }
}