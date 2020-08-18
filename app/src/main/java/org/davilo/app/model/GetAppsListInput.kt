package org.davilo.app.model

class GetAppsListInput(
    var user_id: String?,
    val object_id: String?,
    val object_type: String = "Category"
)
