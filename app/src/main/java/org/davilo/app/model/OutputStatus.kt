package org.davilo.app.model

data class OutputStatus(
    val success: Boolean,
    var status_code: Int,
    var status_message: String
)

