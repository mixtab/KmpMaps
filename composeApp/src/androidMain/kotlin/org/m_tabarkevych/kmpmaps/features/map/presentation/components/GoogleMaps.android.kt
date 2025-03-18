package org.m_tabarkevych.kmpmaps.features.map.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.R
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapAction
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.isDarkTheme

@Composable
actual fun GoogleMaps(
    isSatelliteView: Boolean,
    theme: Theme,
    isMyLocationEnabled: Boolean,
    userLocation: Coordinates?,
    currentMarker: MarkerUi?,
    markers: List<MarkerUi>,
    routeInfo: RouteInfo?,
    mapActionSharedFlow: SharedFlow<MapAction>,
    onMapClicked: () -> Unit,
    onMapLongClicked: (Coordinates) -> Unit,
    onMarkerClicked: (MarkerUi) -> Unit
) {
    val context = LocalContext.current
    val kyiv = LatLng(50.450001, 30.523333)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kyiv, 13f)
    }

    LaunchedEffect(Unit) {
        mapActionSharedFlow.collectLatest {
            when (it) {
                is MapAction.ZoomToLocation -> {
                    val update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(it.location.lat, it.location.lng),
                            cameraPositionState.position.zoom
                        )
                    )
                    cameraPositionState.animate(update)
                }

                is MapAction.ZoomToBounds ->  {
                    val bounds = LatLngBounds.Builder().apply {
                        it.bounds.forEach { coordinate ->
                            include(coordinate.toLatLng())
                        }
                    }.build()

                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngBounds(bounds, 200),
                        durationMs = 1000
                    )
                }
            }
        }

    }
    LaunchedEffect(cameraPositionState.cameraMoveStartedReason) {
        if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            onMapClicked.invoke()
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = isMyLocationEnabled,
            mapType = if (isSatelliteView) MapType.HYBRID else MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                if (theme.isDarkTheme()) R.raw.google_map_style_night else R.raw.google_map_style
            )
        ),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        ),
        onMapClick = {},
        onMapLongClick = {
            onMapLongClicked.invoke(Coordinates(it.latitude, it.longitude))
        },
    ) {
        routeInfo?.let { routeInfo ->
            Polyline(
                points = routeInfo.routePoints.map { it.toLatLng() }, color = Color.Blue
            )
            Marker(
                state = MarkerState(
                    position = routeInfo.startCoordinates.toLatLng()
                ),
                icon = bitmapDescriptorFromVector(context, R.drawable.ic_stop_marker),
            )
            Marker(
                state = MarkerState(
                    position = routeInfo.endCoordinates.toLatLng()
                ),
                icon = bitmapDescriptorFromVector(context, R.drawable.ic_stop_marker),
            )
            return@GoogleMap
        }
        currentMarker?.let {
            Marker(
                state = MarkerState(
                    position = LatLng(it.latitude, it.longitude)
                ),
                icon = bitmapDescriptorFromVector(
                    context,
                    if (it.isBookMarked) R.drawable.ic_saved_marker else R.drawable.ic_marker
                ),
                zIndex = 1.0f,
                title = it.title,
                snippet = it.comment
            )
        }
        markers.forEach { marker ->
            Marker(
                state = MarkerState(
                    position = LatLng(marker.latitude, marker.longitude)
                ),
                icon = bitmapDescriptorFromVector(context, R.drawable.ic_saved_marker),
                zIndex = 1.0f,
                title = marker.title,
                snippet = marker.comment,
                onClick = {
                    onMarkerClicked.invoke(marker)
                    true
                }
            )
        }
    }
}

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

fun Coordinates.toLatLng(): LatLng = LatLng(lat, lng)
