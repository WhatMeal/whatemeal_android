package com.beside.whatmeal.main.uimodel

interface MainItem {
    val text: String
    val id: Int
}

enum class Basic(override val text: String, override val id: Int) : MainItem {
    RICE("밥", 0),
    NOODLE("면", 1),
    BREAD("빵", 2),
    ETC("그외", 3)
}

enum class Soup(override val text: String, override val id: Int) : MainItem {
    SOUP("국물 O", 0),
    NO_SOUP("국물 X", 1)
}

enum class Cook(override val text: String, override val id: Int) : MainItem {
    SIMMER("삶기", 0),
    GRILL("굽기", 1),
    FRY("튀기기", 2),
    STIR_FRY("볶기", 3),
    BOIL("끓이기", 4),
    RAW("날 것", 5),
    SALT("절이기", 6)
}

enum class Ingredient(override val text: String, override val id: Int) : MainItem {
    BEEF("소고기", 0),
    PORK("돼지고기", 1),
    CHICKEN("닭고기", 2),
    VEGETABLE("채소", 3),
    FISH("생선", 4),
    DAIRY_PRODUCT("유제품", 5),
    CRUSTACEAN("갑각류", 6)
}

enum class State(override val text: String, override val id: Int) : MainItem {
    EXCITED("신남", 0),
    STRESS("스트레스",1),
    COLD("추움", 2),
    NORMAL("보통", 3),
    HOT("더움", 4),
    GLOOMY("우울함", 5),
    HUNGRY("배고픔", 6)
}