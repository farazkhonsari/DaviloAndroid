package org.davilo.app.model

data class Level(
    val id: String?,
    val name: String?,
    val is_enroll: Boolean,
    val total_apps: Int?,
    val complete_apps: Int?,
    val description: String?
) {
    fun getNumberOfAppsString(): String {
        return total_apps.toString();
    }
}
