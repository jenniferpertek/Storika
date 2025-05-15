package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.LocationApi

class LocationRepository(private val api: LocationApi) {

    suspend fun getAll() = api.getAllLocations()

    suspend fun get(id: Long) = api.getLocationById(id)

}