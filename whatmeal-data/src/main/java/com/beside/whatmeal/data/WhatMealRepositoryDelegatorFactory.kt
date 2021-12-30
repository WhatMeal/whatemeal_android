package com.beside.whatmeal.data

import android.content.Context
import androidx.annotation.Keep
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface
import com.linecorp.lich.component.DelegatedComponentFactory
import com.linecorp.lich.component.getComponent

@Keep
class WhatMealRepositoryDelegatorFactory :
    DelegatedComponentFactory<WhatMealRepositoryInterface>() {
    override fun createComponent(context: Context): WhatMealRepositoryInterface {
        val dataRepository = context.getComponent(WhatMealRepository)
        return WhatMealRepositoryDelegator(dataRepository)
    }
}