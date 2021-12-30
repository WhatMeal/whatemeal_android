package com.beside.whatmeal.presentation.main.view

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.beside.whatmeal.presentation.main.uimodel.*

class MainScreenPreviewParameterProvider :
    PreviewParameterProvider<MainScreenPreviewParameterProvider.MainScreenParams> {
    override val values: Sequence<MainScreenParams> = sequenceOf(
        BASIC, SOUP, COOK, INGREDIENT, STATE
    )

    companion object {
        private val BASIC = MainScreenParams(
            roundState = MainRoundState.BASIC,
            allItems = Basic.values().toList(),
            selectedItems = listOf(Basic.BREAD)
        )

        private val SOUP = MainScreenParams(
            roundState = MainRoundState.SOUP,
            allItems = Soup.values().toList(),
            selectedItems = listOf(Soup.SOUP)
        )

        private val COOK = MainScreenParams(
            roundState = MainRoundState.COOK,
            allItems = Cook.values().toList(),
            selectedItems = listOf(Cook.BOIL, Cook.FRY)
        )

        private val INGREDIENT = MainScreenParams(
            roundState = MainRoundState.INGREDIENT,
            allItems = Ingredient.values().toList(),
            selectedItems = listOf(Ingredient.BEEF, Ingredient.CHICKEN)
        )

        private val STATE = MainScreenParams(
            roundState = MainRoundState.STATE,
            allItems = State.values().toList(),
            selectedItems = listOf()
        )
    }

    data class MainScreenParams(
        val roundState: MainRoundState,
        val allItems: List<MainItem>,
        val selectedItems: List<MainItem>,
        val onUpButtonClick: () -> Unit = {},
        val onOptionSelect: (MainItem) -> Unit = {},
        val onNextClick: () -> Unit = {}
    )
}