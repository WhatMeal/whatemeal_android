package com.beside.whatmeal.presentation.main.uimodel

interface MainItem {
    val text: String
}

// @TODO: Please remove the id in the UI data.
enum class Basic(override val text: String) : MainItem {
    RICE("밥"),
    NOODLE("면"),
    BREAD("빵"),
    ETC("그외")
}

enum class Soup(override val text: String) : MainItem {
    SOUP("국물 O"),
    NO_SOUP("국물 X")
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