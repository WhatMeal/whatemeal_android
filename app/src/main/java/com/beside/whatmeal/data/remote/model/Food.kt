package com.beside.whatmeal.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// @TODO: Separate data for each layer.
@Parcelize
data class Food(val name: String, val imageUrl: String) : Parcelable