package org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.core.presentation.launchUrl
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import kotlin.time.Duration.Companion.seconds

@Composable
fun RouteInfoBottomSheetContent(
    routeInfo: RouteInfo,
    onRouteDismissClicked: () -> Unit
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = routeInfo.durationInSeconds.seconds.toString(),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = routeInfo.getFormattedDistance(),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            KmpButton(
                modifier = Modifier.weight(1f).padding(16.dp),
                "Start Navigation"
            ) {
                launchUrl(routeInfo.googleUrl)
            }
            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onRouteDismissClicked.invoke()
                    },
                text = "Dismiss",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}