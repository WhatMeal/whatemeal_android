package com.beside.whatmeal.main.viewmodel

import androidx.lifecycle.LiveData
import com.beside.whatmeal.main.uimodel.MainItem
import com.beside.whatmeal.main.uimodel.MainRoundState

interface MainViewModelInterface {
    val mainRoundState: LiveData<MainRoundState>
    val allItems: LiveData<List<MainItem>>
    val selectedItems: LiveData<List<MainItem>>

    fun onNextClick()
    fun onBackPressed(runOSOnBackPressed: () -> Unit)
    fun onUpButtonClick()
    fun onOptionSelect(mainItem: MainItem)
}