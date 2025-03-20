package org.m_tabarkevych.kmpmaps.features.core.presentation

import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenURLOptionsOpenInPlaceKey

actual fun launchNavigationByCoordinates(startCoordinates: Coordinates, endCoordinates: Coordinates) {
    val appleMapsLink = NSURL(string = "http://maps.apple.com/?daddr=${endCoordinates.lat},${endCoordinates.lng}&directionsmode=driving")
    if (UIApplication.sharedApplication.canOpenURL(appleMapsLink)) {
        // Використовуємо новий метод для відкриття Apple Maps
        UIApplication.sharedApplication.openURL(
            appleMapsLink,
            options = mapOf(UIApplicationOpenURLOptionsOpenInPlaceKey to true),
            completionHandler = null
        )
    } else {
        println("Неможливо відкрити жодну картографічну програму.")
    }
}