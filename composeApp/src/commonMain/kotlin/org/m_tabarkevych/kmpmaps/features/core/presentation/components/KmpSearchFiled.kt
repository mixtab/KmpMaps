package org.m_tabarkevych.kmpmaps.features.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.close_hint
import kmpmaps.composeapp.generated.resources.search_hint
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.features.core.presentation.DesertWhite
import org.m_tabarkevych.kmpmaps.features.core.presentation.DodgerBlue

@Composable
fun KmpTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester = FocusRequester(),
    maxLines: Int = 1,
    singleLine:Boolean = true,
    clearButtonEnabled:Boolean = true,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    onImeSearch: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            cursorColor = DodgerBlue,
            focusedBorderColor = DodgerBlue
        ),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        keyboardActions = KeyboardActions(
            onSearch = {
                onImeSearch?.invoke()
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotBlank() && clearButtonEnabled,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.close_hint),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusEvent {
                onFocusChanged?.invoke(it.isFocused)
            }
            .background(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background
            )
            .minimumInteractiveComponentSize()
    )
}

@Composable
@Preview
fun KmpSearchFiledPreview() {
    KmpTextField(
        value = "Hi there ^-^",
        onValueChange = {},
        onFocusChanged = {},
        onImeSearch = {}
    )
}