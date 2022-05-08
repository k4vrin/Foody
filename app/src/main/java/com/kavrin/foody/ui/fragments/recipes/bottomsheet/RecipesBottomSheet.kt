package com.kavrin.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kavrin.foody.R
import com.kavrin.foody.databinding.FragmentRecipesBottomSheetBinding
import com.kavrin.foody.util.Constants.DEFAULT_DIET_TYPE
import com.kavrin.foody.util.Constants.DEFAULT_MEAL_TYPE
import com.kavrin.foody.viewmodels.RecipesViewModel

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private val mRecipesViewModel: RecipesViewModel by activityViewModels()

    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

        // Observe MealAndDietType and update selected chip accordingly
        mRecipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) {
            dietTypeChip = it.selectedDietType
            mealTypeChip = it.selectedMealType
            updateChip(it.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        // Find the selected mealTypeChip
        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds[0])
            mealTypeChip = chip.text.toString().lowercase()
            mealTypeChipId = checkedIds[0]
        }

        // Find the selected dietTypeChip
        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds[0])
            dietTypeChip = chip.text.toString().lowercase()
            dietTypeChipId = checkedIds[0]
        }

        // Save MealAndDietType and navigate back to RecipesFragment
        binding.applyButton.setOnClickListener {
            mRecipesViewModel.saveMealAndDietType(
                mealType = mealTypeChip,
                mealTypeId = mealTypeChipId,
                dietType = dietTypeChip,
                dietTypeId = dietTypeChipId
            )
            // We add safeArg to tell RecipesFragment readDatabase to request new data from api whenever we come back from BottomSheet and have a new preferences
            val action = RecipesBottomSheetDirections
                .actionRecipesBottomSheetToRecipesFragment(backFromBottomSheet = true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", "updateChip: e:${e.message}")
            }
        }

    }

    override fun onDestroyView() {
        Log.d("RecipesBottomSheet", "destroy")
        super.onDestroyView()
        _binding = null
    }
}