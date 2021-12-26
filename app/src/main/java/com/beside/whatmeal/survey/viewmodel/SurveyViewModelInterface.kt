package com.beside.whatmeal.survey.viewmodel

import androidx.lifecycle.LiveData
import com.beside.whatmeal.survey.uimodel.SurveyItem
import com.beside.whatmeal.survey.uimodel.SurveyRoundState

interface SurveyViewModelInterface {
    val surveyRoundState: LiveData<SurveyRoundState>
    val allItems: LiveData<List<SurveyItem>>
    val selectedItems: LiveData<List<SurveyItem>>

    fun onNextClick()
    fun onUpButtonClick()
    fun onBackPressed(runOSOnBackPressed: () -> Unit)
    fun onOptionSelect(surveyItem: SurveyItem)
}