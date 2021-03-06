package com.beside.whatmeal.data.remote.remotemodel

import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.response.LoadFoodListResponse

data class FoodListPagingData (
    val nextRequest: LoadFoodListRequest?,
    val hasNext: Boolean
) {
    companion object {
        fun createBy(
            request: LoadFoodListRequest,
            response: LoadFoodListResponse
        ): FoodListPagingData {
            val nextRequest = if (response.hasNext) {
                request.copy(page = response.page + 1)
            } else {
                null
            }
            return FoodListPagingData(
                nextRequest, response.hasNext
            )
        }
    }
}

fun FoodListPagingData.copyBy(response: LoadFoodListResponse): FoodListPagingData {
    val nextRequest = if (response.hasNext) {
        nextRequest?.copy(page = response.page + 1)
    } else {
        nextRequest?.copy(page = 1)
    }
    return FoodListPagingData(
        nextRequest, response.hasNext
    )
}