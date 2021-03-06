package com.beside.whatmeal.presentation.foodlist.view

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodItem
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListPagingState

class FoodListScreenPreviewParameterProvider :
    PreviewParameterProvider<FoodListScreenPreviewParameterProvider.FoodListScreenParams> {
    override val values: Sequence<FoodListScreenParams> = sequenceOf(
        LOADING, HAS_NEXT, NO_NEXT
    )

    companion object {
        private val DUMMY_FOOD_ITEM_LIST: List<List<FoodItem>> = listOf(
            listOf(
                FoodItem(
                    "치킨",
                    "https://cdn.eyesmag.com/content/uploads/posts/2021/03/10/main-aecd9fb4-f0b1-4115-94af-b87daaf34f33.jpg"
                ),
                FoodItem(
                    "피자",
                    "https://img.hankyung.com/photo/202108/99.26501439.1-1200x.jpg"
                ),
                FoodItem(
                    "족발",
                    "https://static.hubzum.zumst.com/hubzum/2019/07/26/11/8291a05e16b14e9b91eedc7a4375c934_780x585.jpg"
                ),
                FoodItem(
                    "보쌈",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2015/11/04/2822d0962875bb52265b4e90ca1d92ae1.jpg"
                ),
                FoodItem(
                    "돼지고기",
                    "https://img.etoday.co.kr/pto_db/2019/07/600/20190726164728_1350748_727_485.jpg"
                )
            ),
            listOf(
                FoodItem(
                    "짜장면",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2020/06/04/d96e1e81ecc8d86c922d486ec6eec4da1.jpg"
                ),
                FoodItem(
                    "짬뽕",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2017/06/19/2756808e5603db7a18c4f5ee9a699ee41.jpg"
                ),
                FoodItem(
                    "로제 떡볶이",
                    "http://storage.enuri.info/pic_upload/knowbox2/202107/050513263202107082eb27e43-aedc-4275-9af3-f0b50c6f30f8.JPEG"
                ),
                FoodItem(
                    "파스타",
                    "https://blog.kakaocdn.net/dn/cK76SU/btqRCV7FVkE/wKt15KOhO4Mt1ibHq7j430/img.jpg"
                ),
                FoodItem(
                    "라면",
                    "https://mblogthumb-phinf.pstatic.net/MjAyMDA1MjZfNDMg/MDAxNTkwNDgxOTc1OTE1.rpezhYUxGHG0X6-dmVwuACnqm7AugH9CUjxEatcVNsAg.C27glH_kXPk5zTORLyjUbU_yjkGDEbmwZXjaq_xGltIg.JPEG.naverschool/%ED%98%B8%EB%A1%9C%EB%A1%9C%EB%A1%9D.jpg?type=w800"
                )
            ),
            listOf(
                FoodItem(
                    "탕수육",
                    "https://homecuisine.co.kr/files/attach/images/142/737/002/969e9f7dc60d42510c5c0353a58ba701.JPG"
                ),
                FoodItem(
                    "커리",
                    "https://post-phinf.pstatic.net/MjAxNzAyMDhfMTk1/MDAxNDg2NTMxOTAzOTEw.OEsmL1LoHpngnkOBuIJYlg12M5TixHTj_ToPi9Ca-xog.Kan-5YVw8EuwUiLpIJ9MKAwmti8lO6GXeeHq7W0rj94g.JPEG/18.jpg?type=w1200"
                ),
                FoodItem(
                    "쌀국수",
                    "https://ww.namu.la/s/6a370f38311ee224f8ad355ecda68a394b89b844c2c31ec7a6fc3b6dd98ad9094df30565ae25bc24b837aed11cbe92e05273d58a3bcf1c8eb6cf36644e6f8298978ec6ac96b0d1bfca26bded65df8402"
                ),
                FoodItem(
                    "초밥",
                    "https://rimage.gnst.jp/livejapan.com/public/article/detail/a/00/00/a0000370/img/basic/a0000370_main.jpg?20201002142956&q=80&rw=750&rh=536"
                ),
                FoodItem(
                    "찜닭",
                    "https://static.wtable.co.kr/image-resize/production/service/recipe/235/4x3/7c2b5692-bf30-474b-b056-48a2827cbded.jpg"
                )
            )
        )

        private val HAS_NEXT = FoodListScreenParams(
            foodItemList = DUMMY_FOOD_ITEM_LIST[0],
            selectedFoodItem = DUMMY_FOOD_ITEM_LIST[0][0],
            pagingState = FoodListPagingState.HAS_NEXT
        )

        private val NO_NEXT = FoodListScreenParams(
            foodItemList = DUMMY_FOOD_ITEM_LIST[0],
            selectedFoodItem = null,
            pagingState = FoodListPagingState.NO_NEXT
        )

        private val LOADING = FoodListScreenParams(
            foodItemList = DUMMY_FOOD_ITEM_LIST[0],
            selectedFoodItem = null,
            pagingState = FoodListPagingState.LOADING
        )
    }

    data class FoodListScreenParams(
        val foodItemList: List<FoodItem>,
        val selectedFoodItem: FoodItem?,
        val pagingState: FoodListPagingState,
        val onFoodSelect: (FoodItem) -> Unit = {},
        val onRefreshClick: () -> Unit = {},
        val onNextClick: () -> Unit = {}
    )
}