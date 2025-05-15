package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.LocationDto
import retrofit2.http.*

interface LocationApi {

    @GET("/v1/locations")
    suspend fun getAllLocations(): List<LocationDto>

    @GET("/v1/location/{locationId}")
    suspend fun getLocationById(@Path("locationId") locationId: Long): LocationDto

}