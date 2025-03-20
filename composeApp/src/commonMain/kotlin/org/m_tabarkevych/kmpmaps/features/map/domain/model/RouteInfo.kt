package org.m_tabarkevych.kmpmaps.features.map.domain.model

import kotlin.math.pow


data class RouteInfo (
    val durationInSeconds: Long,
    val distanceInMeters:Long,
    val routePoints:List<Coordinates>
) {
    val startCoordinates = routePoints.first()
    val endCoordinates = routePoints.last()

    fun getFormattedDistance(): String {
        return if (distanceInMeters >= 1000) {
            "${(distanceInMeters / 1000.0).roundTo(1)} km"
        } else {
            "$distanceInMeters m"
        }
    }

    private fun Double.roundTo(decimals: Int): String {
        val factor = 10.0.pow(decimals)
        return ((this * factor).toInt() / factor).toString()
    }

}