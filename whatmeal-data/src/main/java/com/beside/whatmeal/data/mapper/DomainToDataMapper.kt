package com.beside.whatmeal.data.mapper

import com.beside.whatmeal.data.remote.remotemodel.FoodData
import com.beside.whatmeal.data.remote.remotemodel.FoodListPagingData
import com.beside.whatmeal.data.remote.remotemodel.PagedFoodListData
import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.domain.domainmodel.*

private typealias DataAge = com.beside.whatmeal.data.remote.remotemodel.Age
private typealias DataMealTime = com.beside.whatmeal.data.remote.remotemodel.MealTime
private typealias DataStandard = com.beside.whatmeal.data.remote.remotemodel.Standard

private typealias DataBasic = com.beside.whatmeal.data.remote.remotemodel.Basic
private typealias DataSoup = com.beside.whatmeal.data.remote.remotemodel.Soup
private typealias DataCook = com.beside.whatmeal.data.remote.remotemodel.Cook
private typealias DataIngredient = com.beside.whatmeal.data.remote.remotemodel.Ingredient
private typealias DataState = com.beside.whatmeal.data.remote.remotemodel.State

fun Age.toDataModel(): DataAge = when (this) {
    Age.TWENTIES -> DataAge.TWENTIES
    Age.THIRTIES -> DataAge.THIRTIES
    Age.PRIVATE -> DataAge.PRIVATE
    Age.OVER_FIFTY -> DataAge.OVER_FIFTY
    Age.FORTIES -> DataAge.FORTIES
    Age.TEENAGE -> DataAge.TEENAGE
}

fun DataAge.toDomainModel(): Age = when (this) {
    DataAge.TWENTIES -> Age.TWENTIES
    DataAge.THIRTIES -> Age.THIRTIES
    DataAge.PRIVATE -> Age.PRIVATE
    DataAge.OVER_FIFTY -> Age.OVER_FIFTY
    DataAge.FORTIES -> Age.FORTIES
    DataAge.TEENAGE -> Age.TEENAGE
}

fun MealTime.toDataModel(): DataMealTime = when (this) {
    MealTime.LUNCH -> DataMealTime.LUNCH
    MealTime.DINNER -> DataMealTime.DINNER
    MealTime.BREAKFAST -> DataMealTime.BREAKFAST
}

fun DataMealTime.toDomainModel(): MealTime = when (this) {
    DataMealTime.BREAKFAST -> MealTime.BREAKFAST
    DataMealTime.DINNER -> MealTime.DINNER
    DataMealTime.LUNCH -> MealTime.LUNCH
}

fun Standard.toDataModel(): DataStandard = when (this) {
    Standard.TIME -> DataStandard.TIME
    Standard.TASTE -> DataStandard.TASTE
    Standard.REVIEW -> DataStandard.REVIEW
    Standard.PRICE -> DataStandard.PRICE
    Standard.PERSON -> DataStandard.PERSON
}

fun DataStandard.toDomainModel(): Standard = when (this) {
    DataStandard.TIME -> Standard.TIME
    DataStandard.TASTE -> Standard.TASTE
    DataStandard.REVIEW -> Standard.REVIEW
    DataStandard.PRICE -> Standard.PRICE
    DataStandard.PERSON -> Standard.PERSON
}

fun Basic.toDataModel(): DataBasic = when (this) {
    Basic.BREAD -> DataBasic.BREAD
    Basic.ETC -> DataBasic.ETC
    Basic.NOODLE -> DataBasic.NOODLE
    Basic.RICE -> DataBasic.RICE
}

fun DataBasic.toDomainModel(): Basic = when (this) {
    DataBasic.BREAD -> Basic.BREAD
    DataBasic.ETC -> Basic.ETC
    DataBasic.NOODLE -> Basic.NOODLE
    DataBasic.RICE -> Basic.RICE
}

fun Soup.toDataModel(): DataSoup = when (this) {
    Soup.SOUP -> DataSoup.SOUP
    Soup.NO_SOUP -> DataSoup.NO_SOUP
}

fun DataSoup.toDomainModel(): Soup = when (this) {
    DataSoup.SOUP -> Soup.SOUP
    DataSoup.NO_SOUP -> Soup.NO_SOUP
}

fun Cook.toDataModel(): DataCook = when (this) {
    Cook.BOIL -> DataCook.BOIL
    Cook.FRY -> DataCook.FRY
    Cook.SIMMER -> DataCook.SIMMER
    Cook.GRILL -> DataCook.GRILL
    Cook.STIR_FRY -> DataCook.STIR_FRY
    Cook.RAW -> DataCook.RAW
    Cook.SALT -> DataCook.SALT
}

fun DataCook.toDomainModel(): Cook = when (this) {
    DataCook.BOIL -> Cook.BOIL
    DataCook.FRY -> Cook.FRY
    DataCook.SIMMER -> Cook.SIMMER
    DataCook.GRILL -> Cook.GRILL
    DataCook.STIR_FRY -> Cook.STIR_FRY
    DataCook.RAW -> Cook.RAW
    DataCook.SALT -> Cook.SALT
}

fun Ingredient.toDataModel(): DataIngredient = when (this) {
    Ingredient.BEEF -> DataIngredient.BEEF
    Ingredient.PORK -> DataIngredient.PORK
    Ingredient.CHICKEN -> DataIngredient.CHICKEN
    Ingredient.VEGETABLE -> DataIngredient.VEGETABLE
    Ingredient.FISH -> DataIngredient.FISH
    Ingredient.DAIRY_PRODUCT -> DataIngredient.DAIRY_PRODUCT
    Ingredient.CRUSTACEAN -> DataIngredient.CRUSTACEAN
}

fun DataIngredient.toDomainModel(): Ingredient = when (this) {
    DataIngredient.BEEF -> Ingredient.BEEF
    DataIngredient.PORK -> Ingredient.PORK
    DataIngredient.CHICKEN -> Ingredient.CHICKEN
    DataIngredient.VEGETABLE -> Ingredient.VEGETABLE
    DataIngredient.FISH -> Ingredient.FISH
    DataIngredient.DAIRY_PRODUCT -> Ingredient.DAIRY_PRODUCT
    DataIngredient.CRUSTACEAN -> Ingredient.CRUSTACEAN
}

fun State.toDataModel(): DataState = when (this) {
    State.EXCITED -> DataState.EXCITED
    State.STRESS -> DataState.STRESS
    State.COLD -> DataState.COLD
    State.NORMAL -> DataState.NORMAL
    State.HOT -> DataState.HOT
    State.GLOOMY -> DataState.GLOOMY
    State.HUNGRY -> DataState.HUNGRY
}

fun DataState.toDomainModel(): State = when (this) {
    DataState.EXCITED -> State.EXCITED
    DataState.STRESS -> State.STRESS
    DataState.COLD -> State.COLD
    DataState.NORMAL -> State.NORMAL
    DataState.HOT -> State.HOT
    DataState.GLOOMY -> State.GLOOMY
    DataState.HUNGRY -> State.HUNGRY
}

fun FoodModel.toDataModel(): FoodData = FoodData(name, imageUrl)

fun FoodData.toDomainModel(): FoodModel = FoodModel(name, imageUrl)

fun FoodListPagingModel.toDataModel(): FoodListPagingData = FoodListPagingData(
    nextRequest = if (basics == null ||
        soup == null ||
        cooks == null ||
        ingredients == null ||
        states == null ||
        page == null
    ) {
        null
    } else {
        LoadFoodListRequest(
            basics as String,
            soup as String,
            cooks as String,
            ingredients as String,
            states as String,
            page as Int
        )
    },
    hasNext
)

fun FoodListPagingData.toDomainModel(): FoodListPagingModel = FoodListPagingModel(
    nextRequest?.basics,
    nextRequest?.soup,
    nextRequest?.cooks,
    nextRequest?.ingredients,
    nextRequest?.states,
    nextRequest?.page,
    hasNext
)

fun PagedFoodListModel.toDataModel(): PagedFoodListData = PagedFoodListData(
    foodList.map { it.toDataModel() },
    pagingData.toDataModel()
)

fun PagedFoodListData.toDomainModel(): PagedFoodListModel = PagedFoodListModel(
    foodList.map { it.toDomainModel() },
    pagingData.toDomainModel()
)