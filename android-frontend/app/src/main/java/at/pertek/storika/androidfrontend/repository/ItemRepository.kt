package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.ItemApi
import at.pertek.storika.androidfrontend.dto.ItemDto

class ItemRepository(private val api: ItemApi) {

    suspend fun getAll() = api.getAllItems()

    suspend fun get(id: Long) = api.getItemById(id)

    suspend fun create(dto: ItemDto) = api.createItem(dto)

    suspend fun update(id: Long, dto: ItemDto) = api.updateItem(id, dto)

    suspend fun delete(id: Long) = api.deleteItem(id)

}