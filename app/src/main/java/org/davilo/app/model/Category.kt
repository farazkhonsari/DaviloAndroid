package org.davilo.app.model

data class Category(
    val id: String?,
    val category: String?,
    val NumberOfCategoryApps: Int?,
    val NumberOfCompleteCategoryApps: Int
) {
    fun getCompletedNumberString(): String {
        return NumberOfCompleteCategoryApps.toString() + "/" + NumberOfCategoryApps.toString()
    }
}
