package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.CategoryDto
import retrofit2.Call
import retrofit2.http.GET

interface InventoryApi {
    @GET("/inventory/categories")
    fun getAllCategories(): Call<List<CategoryDto>>
}