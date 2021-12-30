package com.beside.whatmeal.data.remote.remotemodel.request

import com.beside.whatmeal.data.remote.remotemodel.Age
import com.beside.whatmeal.data.remote.remotemodel.MealTime
import com.beside.whatmeal.data.remote.remotemodel.Standard

data class RegisterTrackingIdRequest(
    val age: Age,
    val mealTime: MealTime,
    val standards: List<Standard>
)