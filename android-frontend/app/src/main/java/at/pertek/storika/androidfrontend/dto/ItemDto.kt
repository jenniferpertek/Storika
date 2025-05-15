package at.pertek.storika.androidfrontend.dto

import java.time.LocalDate

data class ItemDto (
    val id: Long? = null,
    val name: String,
    val quantity: Int,
    val expirationDate: LocalDate,
    val categoryId: Long,
    val compartmentId: Long
)