package org.davilo.app.model

data class Category(
    val id: String?,
    val category: String?,
    val NumberOfCategoryApps: Int?,
    val NumberOfCompletedCategoryApps: Int
) {
    fun getCompletedNumberString(): String {
        return NumberOfCompletedCategoryApps.toString() + "/" + NumberOfCategoryApps.toString()
    }
}
