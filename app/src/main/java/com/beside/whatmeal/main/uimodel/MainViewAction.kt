package com.beside.whatmeal.main.uimodel

sealed class MainViewAction {
    data class StartFoodListScreenAction(
        val basics: String,
        val soup: String,
        val cooks: String,
        val ingredients: String,
        val states: String
    ) : MainViewAction()
}