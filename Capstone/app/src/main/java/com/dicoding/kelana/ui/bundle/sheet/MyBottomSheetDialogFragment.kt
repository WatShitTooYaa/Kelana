package com.dicoding.kelana.ui.bundle.sheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kelana.databinding.BotomSheetScrollLayoutBinding
import com.dicoding.kelana.ui.bundle.BundleViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BotomSheetScrollLayoutBinding? = null
    private val binding get() = _binding!!
    var current = ""
    private lateinit var bundleViewModel: BundleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BotomSheetScrollLayoutBinding.inflate(inflater, container, false)

        bundleViewModel = ViewModelProvider(requireParentFragment())[BundleViewModel::class.java]

        setButtonClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chipGroupCity = binding.cgBottomSheetCity
        val chipGroupPreferences = binding.cgBottomSheetPreference
        // Handle interactions
        binding.btnSheetApplyFilter.setOnClickListener {
            val selectedChipCityId = chipGroupCity.checkedChipIds
            val selectedChipCityText = selectedChipCityId.map { id ->
                val chip: Chip = chipGroupCity.findViewById(id)
                chip.text
            }
            val selectedChipPreferencesId = chipGroupPreferences.checkedChipIds
            val selectedChipPreferencesText = selectedChipPreferencesId.map { id ->
                val chip: Chip = chipGroupPreferences.findViewById(id)
                chip.text
            }
            Toast.makeText(requireContext(), "Selected Chips: $selectedChipCityText + $selectedChipPreferencesText", Toast.LENGTH_SHORT).show()
            // Handle button action
            bundleViewModel.getDummyData()
            dismiss()
        }

        val behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.peekHeight = 300
        behavior.state = BottomSheetBehavior.STATE_EXPANDED


        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun setButtonClick(){
        binding.textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSheetApplyFilter.isEnabled = s?.length != 0
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    binding.textInputEditText.removeTextChangedListener(this)

                    val cleanString = s.toString().replace(".", "")
                    val formattedString = formatNumber(cleanString)

                    current = formattedString
                    binding.textInputEditText.setText(formattedString)
                    binding.textInputEditText.setSelection(formattedString.length)

                    binding.textInputEditText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun formatNumber(number: String): String {
        return number.toLongOrNull()?.let {
            java.text.DecimalFormat("#,###").format(it)
        } ?: number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}