package com.beside.whatmeal.presentation.main.uimodel

sealed class MainViewAction {
    data class StartFoodListScreen(
        val basics: List<Basic>,
        val soup: Soup,
        val cooks: List<Cook>,
        val ingredients: List<Ingredient>,
        val states: List<State>
    ) : MainViewAction()

    object FailToRegisterTrackingId : MainViewAction()
}