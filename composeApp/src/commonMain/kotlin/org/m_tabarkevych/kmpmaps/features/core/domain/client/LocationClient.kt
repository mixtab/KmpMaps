package org.m_tabarkevych.kmpmaps.features.core.domain.client

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates

expect class LocationClient(){
    fun getLocationUpdates():Flow<Coordinates>


}