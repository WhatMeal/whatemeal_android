package com.beside.whatmeal.data.remote.remotemodel

import androidx.annotation.IntRange

interface MainData {
    val id: Int
}

enum class Basic(override val id: Int) : MainData {
    RICE(0),
    NOODLE(1),
    BREAD(2),
    ETC(3);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 3) id: Int): Basic =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Soup(override val id: Int) : MainData {
    SOUP(0),
    NO_SOUP(1);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 1) id: Int): Soup =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Cook(override val id: Int) : MainData {
    SIMMER(0),
    GRILL(1),
    FRY(2),
    STIR_FRY(3),
    BOIL(4),
    RAW(5),
    SALT(6);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): Cook =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Ingredient(override val id: Int) : MainData {
    BEEF(0),
    PORK(1),
    CHICKEN(2),
    VEGETABLE(3),
    FISH(4),
    DAIRY_PRODUCT(5),
    CRUSTACEAN(6);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): Ingredient =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class State(override val id: Int) : MainData {
    EXCITED(0),
    STRESS(1),
    COLD(2),
    NORMAL(3),
    HOT(4),
    GLOOMY(5),
    HUNGRY(6);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): State =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}