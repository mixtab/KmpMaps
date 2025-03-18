package org.m_tabarkevych.kmpmaps.features.core.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun RequestLocationPermission(onResult: (Boolean) -> Unit)