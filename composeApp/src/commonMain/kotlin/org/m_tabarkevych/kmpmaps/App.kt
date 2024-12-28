package org.m_tabarkevych.kmpmaps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.screen_map.GoogleMaps

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding()
        ) {
            GoogleMaps()
        }
    }
}