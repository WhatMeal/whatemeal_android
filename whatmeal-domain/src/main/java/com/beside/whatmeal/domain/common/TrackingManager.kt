package com.beside.whatmeal.domain.common

object TrackingManager {
    private var trackingId: String? = null

    fun holdTrackingId(id: String) {
        trackingId = id
    }

    fun getTrackingId(): String = trackingId ?: throw IllegalStateException()
}