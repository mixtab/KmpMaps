package org.m_tabarkevych.kmpmaps.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_distance
import kmpmaps.composeapp.generated.resources.ic_language
import kmpmaps.composeapp.generated.resources.ic_moon
import kmpmaps.composeapp.generated.resources.settings_distance_unit
import kmpmaps.composeapp.generated.resources.settings_language
import kmpmaps.composeapp.generated.resources.settings_logout
import kmpmaps.composeapp.generated.resources.settings_theme
import kmpmaps.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.SlimyGreen
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.Toolbar
import org.m_tabarkevych.kmpmaps.features.settings.presentation.components.SettingItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenRoute(viewModel: SettingsViewModel, navigate: (NavRoute) -> Unit) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.value.bottomSheetVisible) {
        if (state.value.bottomSheetVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    SettingsEffectHandler(viewModel.effect, navigate)

    SettingsScreen(
        uiState = state.value,
        sheetState = sheetState,
        processUiEvent = {
            viewModel.sendUiEvent(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    sheetState: SheetState,
    processUiEvent: (SettingsUiEvent) -> Unit
) {
    if (uiState.bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { processUiEvent.invoke(SettingsUiEvent.OnBottomSheetCloseClicked) },
            sheetState = sheetState
        ) {
            Column {
                LazyColumn {
                    itemsIndexed(uiState.getBottomSheetItems) { index, item ->
                        Row(modifier = Modifier.clickable {
                            processUiEvent.invoke(SettingsUiEvent.OnItemSelected(index))
                        }.padding(16.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(item.first)
                            )
                            if (item.second) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    tint = SlimyGreen,
                                    contentDescription = "Item icon"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Toolbar(
            title = stringResource(Res.string.settings_title),
            onBackClick = {
                processUiEvent.invoke(SettingsUiEvent.OnBackClicked)
            }
        )

        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingItem(
                    modifier = Modifier.padding(top = 28.dp),
                    title = stringResource(Res.string.settings_language),
                    selectedValue = stringResource(uiState.selectedLanguage.title),
                    icon = painterResource(Res.drawable.ic_language),
                    onClicked = {
                        processUiEvent.invoke(SettingsUiEvent.OnLanguageClicked)
                    }
                )
                SettingItem(
                    modifier = Modifier.padding(top = 28.dp),
                    title = stringResource(Res.string.settings_theme),
                    selectedValue = stringResource(uiState.selectedTheme.title),
                    icon = painterResource(Res.drawable.ic_moon),
                    onClicked = {
                        processUiEvent.invoke(SettingsUiEvent.OnThemeClicked)
                    }
                )
                SettingItem(
                    modifier = Modifier.padding(vertical = 28.dp),
                    title = stringResource(Res.string.settings_distance_unit),
                    selectedValue = stringResource(uiState.selectedDistanceUnit.title),
                    icon = painterResource(Res.drawable.ic_distance),
                    onClicked = {
                        processUiEvent.invoke(SettingsUiEvent.OnDistanceUnitClicked)
                    }
                )

            }
        }

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            text = stringResource(Res.string.settings_logout)
        )
    }
}