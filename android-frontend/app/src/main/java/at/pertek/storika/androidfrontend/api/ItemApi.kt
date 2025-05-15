package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.ItemDto
import retrofit2.http.*

interface ItemApi {

    @GET("/v1/items")
    suspend fun getAllItems(): List<ItemDto>

    @POST("/v1/item")
    suspend fun createItem(@Body itemDto: ItemDto): ItemDto

    @GET("/v1/item/{itemId}")
    suspend fun getItemById(@Path("itemId") itemId: Long): ItemDto

    @PATCH("/v1/item/{itemId}")
    suspend fun updateItem(
        @Path("itemId") itemId: Long,
        @Body itemDto: ItemDto
    ): ItemDto

    @DELETE("/v1/item/{itemId}")
    suspend fun deleteItem(@Path("itemId") itemId: Long)

}