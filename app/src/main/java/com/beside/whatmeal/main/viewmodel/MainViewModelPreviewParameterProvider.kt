package com.beside.whatmeal.main.viewmodel

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beside.whatmeal.main.uimodel.*

class MainViewModelPreviewParameterProvider: PreviewParameterProvider<MainViewModelInterface> {
    override val values: Sequence<MainViewModelInterface> = sequenceOf(
        BASIC, SOUP, COOK, INGREDIENT, STATE
    )

    companion object {
        val BASIC = object : MainViewModelInterface {
            override val mainRoundState: LiveData<MainRoundState> =
                MutableLiveData(MainRoundState.BASIC)
            override val allItems: LiveData<List<MainItem>> =
                MutableLiveData(Basic.values().toList())
            override val selectedItems: LiveData<List<MainItem>> =
                MutableLiveData(listOf(Basic.BREAD))

            override fun onNextClick() {}
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) {}
            override fun onUpButtonClick() {}
            override fun onOptionSelect(mainItem: MainItem) {}
        }

        val SOUP = object : MainViewModelInterface {
            override val mainRoundState: LiveData<MainRoundState> =
                MutableLiveData(MainRoundState.SOUP)
            override val allItems: LiveData<List<MainItem>> =
                MutableLiveData(Soup.values().toList())
            override val selectedItems: LiveData<List<MainItem>> =
                MutableLiveData(listOf(Soup.SOUP))

            override fun onNextClick() {}
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) {}
            override fun onUpButtonClick() {}
            override fun onOptionSelect(mainItem: MainItem) {}
        }

        val COOK = object : MainViewModelInterface {
            override val mainRoundState: LiveData<MainRoundState> =
                MutableLiveData(MainRoundState.COOK)
            override val allItems: LiveData<List<MainItem>> =
                MutableLiveData(Cook.values().toList())
            override val selectedItems: LiveData<List<MainItem>> =
                MutableLiveData(listOf(Cook.BOIL, Cook.FRY))

            override fun onNextClick() {}
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) {}
            override fun onUpButtonClick() {}
            override fun onOptionSelect(mainItem: MainItem) {}
        }

        val INGREDIENT = object : MainViewModelInterface {
            override val mainRoundState: LiveData<MainRoundState> =
                MutableLiveData(MainRoundState.INGREDIENT)
            override val allItems: LiveData<List<MainItem>> =
                MutableLiveData(Ingredient.values().toList())
            override val selectedItems: LiveData<List<MainItem>> =
                MutableLiveData(listOf(Ingredient.BEEF, Ingredient.CHICKEN))

            override fun onNextClick() {}
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) {}
            override fun onUpButtonClick() {}
            override fun onOptionSelect(mainItem: MainItem) {}
        }

        val STATE = object : MainViewModelInterface {
            override val mainRoundState: LiveData<MainRoundState> =
                MutableLiveData(MainRoundState.STATE)
            override val allItems: LiveData<List<MainItem>> =
                MutableLiveData(State.values().toList())
            override val selectedItems: LiveData<List<MainItem>> =
                MutableLiveData(listOf())

            override fun onNextClick() {}
            override fun onBackPressed(runOSOnBackPressed: () -> Unit) {}
            override fun onUpButtonClick() {}
            override fun onOptionSelect(mainItem: MainItem) {}
        }
    }
}