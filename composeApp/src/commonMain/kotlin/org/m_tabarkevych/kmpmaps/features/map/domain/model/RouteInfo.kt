package org.m_tabarkevych.kmpmaps.features.map.domain.model


data class RouteInfo (
    val durationInSeconds: Long,
    val distanceInMeters:Long,
    val routePoints:List<Coordinates>
) {

    val startCoordinates = routePoints.first()
    val endCoordinates = routePoints.last()

    val googleUrl = "http://maps.google.com/maps?daddr=${endCoordinates.lat},${endCoordinates.lng}"
}