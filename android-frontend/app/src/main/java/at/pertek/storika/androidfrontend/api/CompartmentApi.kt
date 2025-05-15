package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.CompartmentDto
import retrofit2.http.*

interface CompartmentApi {

    @GET("/v1/compartments")
    suspend fun getAllCompartments(): List<CompartmentDto>

    @GET("/v1/compartment/{compartmentId}")
    suspend fun getCompartmentById(@Path("compartmentId") compartmentId: Long): CompartmentDto

}