package com.beside.whatmeal.survey.viewmodel

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beside.whatmeal.survey.uimodel.*

class SurveyViewModelPreviewParameterProvider : PreviewParameterProvider<SurveyViewModelInterface> {
    override val values: Sequence<SurveyViewModelInterface> = sequenceOf(
        AGE, MEAL_TIME, STANDARD
    )

    companion object {
        val AGE = object : SurveyViewModelInterface {
            override val surveyRoundState: LiveData<SurveyRoundState> =
                MutableLiveData(SurveyRoundState.AGE)
            override val allItems: LiveData<List<SurveyItem>> =
                MutableLiveData(Age.values().toList())
            override val selectedItems: LiveData<List<SurveyItem>> = MutableLiveData(listOf())

            override fun onNextClick() = Unit
            override fun onUpButtonClick() = Unit
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) = Unit
            override fun onOptionSelect(surveyItem: SurveyItem) = Unit
        }

        val MEAL_TIME = object : SurveyViewModelInterface {
            override val surveyRoundState: LiveData<SurveyRoundState> =
                MutableLiveData(SurveyRoundState.MEAL_TIME)
            override val allItems: LiveData<List<SurveyItem>> =
                MutableLiveData(MealTime.values().toList())
            override val selectedItems: LiveData<List<SurveyItem>> =
                MutableLiveData(listOf(MealTime.BREAKFAST))

            override fun onNextClick() = Unit
            override fun onUpButtonClick() = Unit
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) = Unit
            override fun onOptionSelect(surveyItem: SurveyItem) = Unit
        }

        val STANDARD = object : SurveyViewModelInterface {
            override val surveyRoundState: LiveData<SurveyRoundState> =
                MutableLiveData(SurveyRoundState.STANDARD)
            override val allItems: LiveData<List<SurveyItem>> =
                MutableLiveData(Standard.values().toList())
            override val selectedItems: LiveData<List<SurveyItem>> =
                MutableLiveData(listOf(Standard.PRICE, Standard.TASTE))

            override fun onNextClick() = Unit
            override fun onUpButtonClick() = Unit
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) = Unit
            override fun onOptionSelect(surveyItem: SurveyItem) = Unit
        }
    }
}
