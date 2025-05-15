package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.CompartmentApi

class CompartmentRepository(private val api: CompartmentApi) {

    suspend fun getAll() = api.getAllCompartments()

    suspend fun get(id: Long) = api.getCompartmentById(id)

}