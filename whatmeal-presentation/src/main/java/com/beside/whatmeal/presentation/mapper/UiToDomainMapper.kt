package com.beside.whatmeal.presentation.mapper

import com.beside.whatmeal.domain.domainmodel.FoodListPagingModel
import com.beside.whatmeal.domain.domainmodel.FoodModel
import com.beside.whatmeal.domain.domainmodel.PagedFoodListModel
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodItem
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListPagingItem
import com.beside.whatmeal.presentation.foodlist.uimodel.PagedFoodListItem
import com.beside.whatmeal.presentation.main.uimodel.*
import com.beside.whatmeal.presentation.survey.uimodel.Age
import com.beside.whatmeal.presentation.survey.uimodel.MealTime
import com.beside.whatmeal.presentation.survey.uimodel.Standard

private typealias DomainAge = com.beside.whatmeal.domain.domainmodel.Age
private typealias DomainMealTime = com.beside.whatmeal.domain.domainmodel.MealTime
private typealias DomainStandard = com.beside.whatmeal.domain.domainmodel.Standard

private typealias DomainBasic = com.beside.whatmeal.domain.domainmodel.Basic
private typealias DomainSoup = com.beside.whatmeal.domain.domainmodel.Soup
private typealias DomainCook = com.beside.whatmeal.domain.domainmodel.Cook
private typealias DomainIngredient = com.beside.whatmeal.domain.domainmodel.Ingredient
private typealias DomainState = com.beside.whatmeal.domain.domainmodel.State

fun Age.toDomainModel(): DomainAge = when (this) {
    Age.TWENTIES -> DomainAge.TWENTIES
    Age.THIRTIES -> DomainAge.THIRTIES
    Age.PRIVATE -> DomainAge.PRIVATE
    Age.OVER_FIFTY -> DomainAge.OVER_FIFTY
    Age.FORTIES -> DomainAge.FORTIES
    Age.TEENAGE -> DomainAge.TEENAGE
}

fun DomainAge.toUiModel(): Age = when (this) {
    DomainAge.TWENTIES -> Age.TWENTIES
    DomainAge.THIRTIES -> Age.THIRTIES
    DomainAge.PRIVATE -> Age.PRIVATE
    DomainAge.OVER_FIFTY -> Age.OVER_FIFTY
    DomainAge.FORTIES -> Age.FORTIES
    DomainAge.TEENAGE -> Age.TEENAGE
}

fun MealTime.toDomainModel(): DomainMealTime = when (this) {
    MealTime.LUNCH -> DomainMealTime.LUNCH
    MealTime.DINNER -> DomainMealTime.DINNER
    MealTime.BREAKFAST -> DomainMealTime.BREAKFAST
}

fun DomainMealTime.toUiModel(): MealTime = when (this) {
    DomainMealTime.LUNCH -> MealTime.LUNCH
    DomainMealTime.BREAKFAST -> MealTime.BREAKFAST
    DomainMealTime.DINNER -> MealTime.DINNER
}

fun Standard.toDomainModel(): DomainStandard = when (this) {
    Standard.TIME -> DomainStandard.TIME
    Standard.TASTE -> DomainStandard.TASTE
    Standard.REVIEW -> DomainStandard.REVIEW
    Standard.PRICE -> DomainStandard.PRICE
    Standard.PERSON -> DomainStandard.PERSON
}

fun DomainStandard.toUiModel(): Standard = when (this) {
    DomainStandard.TIME -> Standard.TIME
    DomainStandard.TASTE -> Standard.TASTE
    DomainStandard.REVIEW -> Standard.REVIEW
    DomainStandard.PRICE -> Standard.PRICE
    DomainStandard.PERSON -> Standard.PERSON
}

fun Basic.toDomainModel(): DomainBasic = when (this) {
    Basic.BREAD -> DomainBasic.BREAD
    Basic.ETC -> DomainBasic.ETC
    Basic.NOODLE -> DomainBasic.NOODLE
    Basic.RICE -> DomainBasic.RICE
}

fun DomainBasic.toUiModel(): Basic = when (this) {
    DomainBasic.BREAD -> Basic.BREAD
    DomainBasic.ETC -> Basic.ETC
    DomainBasic.NOODLE -> Basic.NOODLE
    DomainBasic.RICE -> Basic.RICE
}

fun Soup.toDomainModel(): DomainSoup = when (this) {
    Soup.SOUP -> DomainSoup.SOUP
    Soup.NO_SOUP -> DomainSoup.NO_SOUP
}

fun DomainSoup.toUiModel(): Soup = when (this) {
    DomainSoup.SOUP -> Soup.SOUP
    DomainSoup.NO_SOUP -> Soup.NO_SOUP
}

fun Cook.toDomainModel(): DomainCook = when (this) {
    Cook.BOIL -> DomainCook.BOIL
    Cook.FRY -> DomainCook.FRY
    Cook.SIMMER -> DomainCook.SIMMER
    Cook.GRILL -> DomainCook.GRILL
    Cook.STIR_FRY -> DomainCook.STIR_FRY
    Cook.RAW -> DomainCook.RAW
    Cook.SALT -> DomainCook.SALT
}

fun DomainCook.toUiModel(): Cook = when (this) {
    DomainCook.BOIL -> Cook.BOIL
    DomainCook.FRY -> Cook.FRY
    DomainCook.SIMMER -> Cook.SIMMER
    DomainCook.GRILL -> Cook.GRILL
    DomainCook.STIR_FRY -> Cook.STIR_FRY
    DomainCook.RAW -> Cook.RAW
    DomainCook.SALT -> Cook.SALT
}

fun Ingredient.toDomainModel(): DomainIngredient = when (this) {
    Ingredient.BEEF -> DomainIngredient.BEEF
    Ingredient.PORK -> DomainIngredient.PORK
    Ingredient.CHICKEN -> DomainIngredient.CHICKEN
    Ingredient.VEGETABLE -> DomainIngredient.VEGETABLE
    Ingredient.FISH -> DomainIngredient.FISH
    Ingredient.DAIRY_PRODUCT -> DomainIngredient.DAIRY_PRODUCT
    Ingredient.CRUSTACEAN -> DomainIngredient.CRUSTACEAN
}

fun DomainIngredient.toUiModel(): Ingredient = when (this) {
    DomainIngredient.BEEF -> Ingredient.BEEF
    DomainIngredient.PORK -> Ingredient.PORK
    DomainIngredient.CHICKEN -> Ingredient.CHICKEN
    DomainIngredient.VEGETABLE -> Ingredient.VEGETABLE
    DomainIngredient.FISH -> Ingredient.FISH
    DomainIngredient.DAIRY_PRODUCT -> Ingredient.DAIRY_PRODUCT
    DomainIngredient.CRUSTACEAN -> Ingredient.CRUSTACEAN
}

fun State.toDomainModel(): DomainState = when (this) {
    State.EXCITED -> DomainState.EXCITED
    State.STRESS -> DomainState.STRESS
    State.COLD -> DomainState.COLD
    State.NORMAL -> DomainState.NORMAL
    State.HOT -> DomainState.HOT
    State.GLOOMY -> DomainState.GLOOMY
    State.HUNGRY -> DomainState.HUNGRY
}

fun DomainState.toUiModel(): State = when (this) {
    DomainState.EXCITED -> State.EXCITED
    DomainState.STRESS -> State.STRESS
    DomainState.COLD -> State.COLD
    DomainState.NORMAL -> State.NORMAL
    DomainState.HOT -> State.HOT
    DomainState.GLOOMY -> State.GLOOMY
    DomainState.HUNGRY -> State.HUNGRY
}

fun FoodModel.toUiModel(): FoodItem = FoodItem(name, imageUrl)

fun FoodItem.toDomainModel(): FoodModel = FoodModel(name, imageUrl)

fun FoodListPagingModel.toUiModel(): FoodListPagingItem =
    FoodListPagingItem(basics, soup, cooks, ingredients, states, page, hasNext)

fun FoodListPagingItem.toDomainModel(): FoodListPagingModel =
    FoodListPagingModel(basics, soup, cooks, ingredients, states, page, hasNext)

fun PagedFoodListModel.toUiModel(): PagedFoodListItem = PagedFoodListItem(
    foodList.map { it.toUiModel() },
    pagingData.toUiModel()
)

fun PagedFoodListItem.toDomainModel(): PagedFoodListModel = PagedFoodListModel(
        foodList.map { it.toDomainModel() },
        pagingItem.toDomainModel()
    )