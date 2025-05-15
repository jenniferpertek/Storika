package at.pertek.storika.androidfrontend.repository

import at.pertek.storika.androidfrontend.api.CategoryApi
import at.pertek.storika.androidfrontend.dto.CategoryDto

class CategoryRepository(private val categoryApi: CategoryApi) {

    suspend fun getAll(): List<CategoryDto> {
        return categoryApi.getAllCategories()
    }

    suspend fun get(id: Long): CategoryDto {
        return categoryApi.getCategoryById(id)
    }

    suspend fun create(dto: CategoryDto): CategoryDto {
        return categoryApi.createCategory(dto)
    }

    suspend fun update(id: Long, dto: CategoryDto): CategoryDto {
        return categoryApi.updateCategory(id, dto)
    }

    suspend fun delete(id: Long) {
        return categoryApi.deleteCategory(id)
    }

}
