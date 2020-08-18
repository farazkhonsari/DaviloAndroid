package org.davilo.app.model

data class GetLevelListInput(
    var user_id: String?,
    val object_type: String = "Levels"
)
