package org.davilo.app.model

data class GetCategoryListInput(
    var user_id: String?,
    val object_id: String?,
    val object_type: String = "Module"
)
