package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.CategoryApi
import at.pertek.storika.androidfrontend.dto.CategoryDto

class CategoryRepository(private val api: CategoryApi) {

    suspend fun getAll() = api.getAllCategories()

    suspend fun get(id: Long) = api.getCategoryById(id)

    suspend fun create(dto: CategoryDto) = api.createCategory(dto)

    suspend fun update(id: Long, dto: CategoryDto) = api.updateCategory(id, dto)

    suspend fun delete(id: Long) = api.deleteCategory(id)

}
