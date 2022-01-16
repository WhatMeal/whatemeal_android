package com.beside.whatmeal.presentation.main.uimodel

import androidx.annotation.DrawableRes
import com.beside.whatmeal.R

interface MainItem {
    val text: String
}

interface MainWithIconItem : MainItem {
    @get:DrawableRes
    val iconDrawableRes: Int
}

enum class Basic(override val text: String, override val iconDrawableRes: Int) : MainWithIconItem {
    RICE("밥", R.drawable.rice),
    ETC("그외", R.drawable.extra),
    NOODLE("면", R.drawable.noodle),
    BREAD("빵", R.drawable.bread)
}

enum class Soup(override val text: String, override val iconDrawableRes: Int) : MainWithIconItem {
    SOUP("국물 있는", R.drawable.soup),
    NO_SOUP("국물 없는", R.drawable.nonsoup)
}

enum class Cook(override val text: String) : MainItem {
    SIMMER("삶기"),
    GRILL("굽기"),
    FRY("튀기기"),
    STIR_FRY("볶기"),
    BOIL("끓이기"),
    RAW("날 것"),
    SALT("절이기")
}

enum class Ingredient(override val text: String) : MainItem {
    BEEF("소고기"),
    PORK("돼지고기"),
    CHICKEN("닭고기"),
    VEGETABLE("채소"),
    FISH("생선"),
    DAIRY_PRODUCT("유제품"),
    CRUSTACEAN("갑각류")
}

enum class State(override val text: String) : MainItem {
    EXCITED("신남"),
    STRESS("스트레스"),
    COLD("추움"),
    NORMAL("보통"),
    HOT("더움"),
    GLOOMY("우울함"),
    HUNGRY("배고픔")
}