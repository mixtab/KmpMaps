package org.m_tabarkevych.kmpmaps.screen_map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMaps() {

    val kyiv = LatLng(	50.450001,30.523333)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(kyiv,10f)
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    )
}