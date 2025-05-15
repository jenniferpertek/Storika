package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.StorageUnitDto
import retrofit2.http.*

interface StorageUnitApi {

    @GET("/v1/storageunits")
    suspend fun getAllStorageUnits(): List<StorageUnitDto>

    @GET("/v1/storageunit/{storageUnitId}")
    suspend fun getStorageUnitById(@Path("storageUnitId") storageUnitId: Long): StorageUnitDto

}