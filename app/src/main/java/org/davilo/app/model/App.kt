package org.davilo.app.model

class App(
    var id: String?,
    var categoryId: String,
    var levelId: String,
    var done: Boolean?,
    var url: String?,
    var description: String?,
    var difficulty: Int?,
    var type: AppType
)
