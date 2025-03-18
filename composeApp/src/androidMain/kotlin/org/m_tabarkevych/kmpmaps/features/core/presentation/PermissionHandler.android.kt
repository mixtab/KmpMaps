package org.m_tabarkevych.kmpmaps.features.core.presentation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun RequestLocationPermission( onResult: (Boolean) -> Unit) {

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onPermissionsResult = { list ->
            onResult(list.all { it.value })
        })


    LaunchedEffect(Unit) {
        locationPermissionState.launchMultiplePermissionRequest()
    }
}

