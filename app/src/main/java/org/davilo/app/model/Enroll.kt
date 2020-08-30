package org.davilo.app.model

data class Enroll(
    val id: String,
    val level: String,
    val modules_info: ArrayList<ModuleInfo>,
    val degree: String?,
    val  description:String?

    )


data class ModuleInfo(
    val id: String,
    val module: String,
    val NumberOfModuleApps: Int,
    val NumberOfCompletedModuleApps: Int

) {
    fun getPercentString(): String {
        return getPercent().toString() + "%"
    }

    fun getPercent(): Int {
        return (NumberOfCompletedModuleApps * 100f / NumberOfModuleApps).toInt()
    }
}
