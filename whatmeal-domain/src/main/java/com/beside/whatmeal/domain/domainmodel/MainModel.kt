package com.beside.whatmeal.domain.domainmodel

interface MainModel

enum class Basic : MainModel {
    RICE, NOODLE, BREAD, ETC
}

enum class Soup : MainModel {
    SOUP, NO_SOUP
}

enum class Cook : MainModel {
    SIMMER, GRILL, FRY, STIR_FRY, BOIL, RAW, SALT
}

enum class Ingredient : MainModel {
    BEEF, PORK, CHICKEN, VEGETABLE, FISH, DAIRY_PRODUCT, CRUSTACEAN
}

enum class State : MainModel {
    EXCITED, STRESS, COLD, NORMAL, HOT, GLOOMY, HUNGRY
}