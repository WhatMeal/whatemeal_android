package com.beside.whatmeal.survey

import com.beside.whatmeal.main.viewmodel.MainViewModel
import com.beside.whatmeal.survey.uimodel.SurveyViewAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SurveyViewActionHandler(private val activity: SurveyActivity) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun postSurveyViewAction(action: SurveyViewAction) = coroutineScope.launch {
        when (action) {
            is SurveyViewAction.StartMainScreenAction -> startMainActivity(action)
        }
    }

    private fun startMainActivity(action: SurveyViewAction.StartMainScreenAction) {
        val intent = MainViewModel.createIntent(
            context = activity,
            action.age,
            action.mealTime,
            action.standards
        )
        activity.startActivity(intent)
    }
}