package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.StorageUnitApi

class StorageUnitRepository(private val api: StorageUnitApi) {

    suspend fun getAll() = api.getAllStorageUnits()

    suspend fun get(id: Long) = api.getStorageUnitById(id)

}
