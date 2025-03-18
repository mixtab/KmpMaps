package org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.add_new_stop
import kmpmaps.composeapp.generated.resources.add_stops
import kmpmaps.composeapp.generated.resources.ic_marker
import kmpmaps.composeapp.generated.resources.ic_plus
import kmpmaps.composeapp.generated.resources.map_add_your_first_stop
import kmpmaps.composeapp.generated.resources.search_hint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpTextField
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.getScreenHeight
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDefaultBottomSheetContent(
    searchValue: String,
    focusRequester: FocusRequester,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    searchResults: List<SearchResult>,
    onSearchValueChanged: (String) -> Unit,
    onSearchResultClicked: (SearchResult) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        Modifier.height((getScreenHeight().value * 0.7).dp)
            .padding(horizontal = 16.dp)
    ) {

        KmpTextField(
            value = searchValue,
            onImeSearch = {},
            placeholder = { Text(text = stringResource(Res.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "leading icon",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f)
                )
            },
            focusRequester = focusRequester,
            onFocusChanged = { hasFocus ->
                scope.launch {
                    if (hasFocus)
                        bottomSheetState.expand()

                }
            },
            onValueChange = { onSearchValueChanged(it) }
        )
        AnimatedVisibility(searchResults.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally)) {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.map_add_your_first_stop)
                )
                KmpButton(
                    modifier = Modifier.padding(bottom = 16.dp).align(Alignment.BottomCenter),
                    icon = Res.drawable.ic_plus,
                    text = stringResource(Res.string.add_stops),
                    onClick = {
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }
                )
            }
        }
        AnimatedVisibility(searchResults.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    text = stringResource(Res.string.add_new_stop)
                )
                LazyColumn {
                    items(searchResults) { item ->
                        Column {
                            Spacer(Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    onSearchResultClicked.invoke(item)
                                }
                            ) {
                                Image(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                        .size(24.dp),
                                    painter = painterResource(Res.drawable.ic_marker),
                                    contentDescription = "Marker"
                                )
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(text = item.title)
                                    Text(text = item.description)
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(40.dp))

    }
}