package com.beside.whatmeal.domain.common

object TrackingManager {
    private var trackingId: Int? = null

    fun holdTrackingId(id: Int) {
        trackingId = id
    }

    fun getTrackingId(): Int = trackingId ?: throw IllegalStateException()
}