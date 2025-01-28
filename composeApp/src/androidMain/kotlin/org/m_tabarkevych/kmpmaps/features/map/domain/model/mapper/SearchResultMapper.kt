package org.m_tabarkevych.kmpmaps.features.map.domain.model.mapper

import com.google.android.libraries.places.api.model.AutocompletePrediction
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult

fun AutocompletePrediction.toSearchResult() = SearchResult(
    this.placeId,
    this.getPrimaryText(null).toString(),
    this.getSecondaryText(null).toString(),
    this.distanceMeters?:0
)

fun List<AutocompletePrediction>.toSearchResult() = this.map { it.toSearchResult() }