package com.beside.whatmeal.data.remote.remotemodel

import androidx.annotation.IntRange

interface MainData {
    val id: Int
}

enum class Basic(override val id: Int) : MainData {
    RICE(1),
    NOODLE(3),
    BREAD(4),
    ETC(2);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 3) id: Int): Basic =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Soup(override val id: Int) : MainData {
    SOUP(1),
    NO_SOUP(2);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 1) id: Int): Soup =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Cook(override val id: Int) : MainData {
    SIMMER(1),
    GRILL(2),
    FRY(3),
    STIR_FRY(4),
    BOIL(5),
    RAW(6),
    SALT(7);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): Cook =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class Ingredient(override val id: Int) : MainData {
    BEEF(1),
    PORK(2),
    CHICKEN(3),
    VEGETABLE(4),
    FISH(5),
    DAIRY_PRODUCT(6),
    CRUSTACEAN(7);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): Ingredient =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}

enum class State(override val id: Int) : MainData {
    EXCITED(1),
    STRESS(2),
    COLD(3),
    NORMAL(4),
    HOT(5),
    GLOOMY(6),
    HUNGRY(7);

    companion object {
        private val ID_INSTANCE_MAP = values().associateBy { it.id }

        fun findBy(@IntRange(from = 0, to = 6) id: Int): State =
            ID_INSTANCE_MAP[id] ?: throw IllegalArgumentException()
    }
}