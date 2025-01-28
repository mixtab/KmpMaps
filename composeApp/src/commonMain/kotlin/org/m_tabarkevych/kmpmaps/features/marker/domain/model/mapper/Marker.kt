package org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper

import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.data.database.enitity.MarkerEntity
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.Marker

fun Marker.toMarkerUi() = MarkerUi(
    id = id,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    title = title,
    address = address,
    comment = comment,
    isBookMarked = true
)

fun List<Marker>.toMarkerUiList() = map { it.toMarkerUi() }

fun MarkerUi.toDomain() = Marker(
    id = id,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    title = title,
    address = address,
    comment = comment
)

fun List<MarkerUi>.toDomain() = map { it.toDomain() }

fun Marker.toEntity() = MarkerEntity(
    id = id,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    title = title,
    address = address,
    comment = comment
)

fun List<Marker>.toEntity() = map { it.toEntity() }


fun MarkerEntity.entityToDomain() = Marker(
    id = id,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    title = title,
    address = address,
    comment = comment
)

fun List<MarkerEntity>.entityToDomain() = map { it.entityToDomain() }


