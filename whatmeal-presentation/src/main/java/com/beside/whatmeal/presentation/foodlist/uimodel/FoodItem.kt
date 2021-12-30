package com.beside.whatmeal.presentation.foodlist.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(val name: String, val imageUrl: String) : Parcelable