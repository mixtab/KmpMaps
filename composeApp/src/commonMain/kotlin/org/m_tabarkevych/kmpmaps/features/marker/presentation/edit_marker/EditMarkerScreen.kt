package org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.cancel_common
import kmpmaps.composeapp.generated.resources.create_route
import kmpmaps.composeapp.generated.resources.edit_marker_address
import kmpmaps.composeapp.generated.resources.edit_marker_comment_label
import kmpmaps.composeapp.generated.resources.edit_marker_coordinates
import kmpmaps.composeapp.generated.resources.edit_marker_title
import kmpmaps.composeapp.generated.resources.edit_marker_title_label
import kmpmaps.composeapp.generated.resources.ic_marker
import kmpmaps.composeapp.generated.resources.remove_marker
import kmpmaps.composeapp.generated.resources.save_common
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpTextField
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.Toolbar
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapUiEvent

@Composable
fun EditMarkerRoute(
    viewModel: EditMarkerViewModel, navigate: (NavRoute) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value


    EditMarkerEffectHandler(viewModel.effect, navigate)
    EditMarkerScreen(uiState, processUiEvent = { viewModel.sendUiEvent(it) })
}

@Composable
fun EditMarkerScreen(uiState: EditMarkerUiState, processUiEvent: (EditMarkerUiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .imePadding()
            .fillMaxWidth()
    ) {
        Toolbar(title = stringResource(Res.string.edit_marker_title), onBackClick = {
            processUiEvent.invoke(EditMarkerUiEvent.OnBackClicked)
        })

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            KmpTextField(
                value = uiState.titleInputValue,
                onValueChange = {
                    processUiEvent(EditMarkerUiEvent.OnTitleChanged(it))
                },
                clearButtonEnabled = false,
                label = {
                    Text(
                        stringResource(Res.string.edit_marker_title_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
            )
            Spacer(Modifier.height(16.dp))
            KmpTextField(
                modifier = Modifier.height(112.dp),
                value = uiState.commentInputValue,
                onValueChange = {
                    processUiEvent(EditMarkerUiEvent.OnCommentChanged(it))
                },
                clearButtonEnabled = false,
                maxLines = 4,
                singleLine = false,
                label = {
                    Text(
                        stringResource(Res.string.edit_marker_comment_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(Res.string.edit_marker_coordinates),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = uiState.marker?.latitude.toString() + ", " + uiState.marker?.longitude.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(Res.string.edit_marker_address),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = uiState.marker?.address ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(100.dp))

        }

        Row(
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp).fillMaxWidth(),
        ) {
            val keyboard = LocalSoftwareKeyboardController.current
            OutlinedButton(
                onClick = {
                    keyboard?.hide()
                    processUiEvent.invoke(EditMarkerUiEvent.OnBackClicked)
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Red),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    stringResource(Res.string.cancel_common),
                    color = Color.Red
                )
            }
            Spacer(Modifier.width(10.dp))

            KmpButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.save_common),
                onClick = {
                    processUiEvent.invoke(EditMarkerUiEvent.OnSaveClicked)
                }
            )
        }
    }
}