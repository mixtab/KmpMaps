package org.m_tabarkevych.kmpmaps.features.core.presentation


import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.error_disk_full
import kmpmaps.composeapp.generated.resources.error_no_internet
import kmpmaps.composeapp.generated.resources.error_request_timeout
import kmpmaps.composeapp.generated.resources.error_serialization
import kmpmaps.composeapp.generated.resources.error_too_many_requests
import kmpmaps.composeapp.generated.resources.error_unknown
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when (this) {
        DataError.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.NO_INTERNET -> Res.string.error_no_internet
        DataError.SERVER -> Res.string.error_unknown
        DataError.SERIALIZATION -> Res.string.error_serialization
        DataError.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.StringResourceId(stringRes)
}