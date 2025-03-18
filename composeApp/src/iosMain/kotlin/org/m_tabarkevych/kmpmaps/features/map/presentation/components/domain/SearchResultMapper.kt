package org.m_tabarkevych.kmpmaps.features.map.presentation.components.domain

import cocoapods.GooglePlaces.GMSPlace
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import platform.CoreLocation.CLLocation

@OptIn(ExperimentalForeignApi::class)
fun GMSPlace.toSearchResult(userPosition: Coordinates): SearchResult {
    val placeLocation = CLLocation(
        latitude = this.coordinate.useContents { latitude },
        longitude = this.coordinate.useContents { longitude })
    val userLocation = CLLocation(latitude = userPosition.lat, longitude = userPosition.lng)

    val distance = placeLocation.distanceFromLocation(userLocation) // ✅ Get distance in meters

    return SearchResult(
        id = this.placeID ?: "",
        title = this.name ?: "Unknown",
        description = this.formattedAddress ?: "",
        distanceInMeters = distance.toInt(),
        coordinates = Coordinates(this.coordinate.useContents { latitude }, this.coordinate.useContents { longitude })
    )
}

@OptIn(ExperimentalForeignApi::class)
fun List<GMSPlace>.toSearchResult(userPosition: Coordinates) =
    this.map { it.toSearchResult(userPosition) }