package org.m_tabarkevych.kmpmaps.features.map.domain.model

data class SearchResult (
    val id:String,
    val title:String,
    val description:String,
    val distanceInMeters:Int
)