package at.pertek.storika.androidfrontend.api

import at.pertek.storika.androidfrontend.dto.CategoryDto
import retrofit2.http.*

interface CategoryApi {

    @GET("/v1/categories")
    suspend fun getAllCategories(): List<CategoryDto>

    @POST("/v1/category")
    suspend fun createCategory(@Body categoryDto: CategoryDto): CategoryDto

    @GET("/v1/category/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: Long): CategoryDto

    @PATCH("/v1/category/{categoryId}")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: Long,
        @Body categoryDto: CategoryDto
    ): CategoryDto

    @DELETE("/v1/category/{categoryId}")
    suspend fun deleteCategory(@Path("categoryId") categoryId: Long)

}