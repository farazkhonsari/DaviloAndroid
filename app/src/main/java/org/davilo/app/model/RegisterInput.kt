package org.davilo.app.model

data class RegisterInput(
    val email: String,
    val password: String,
    val profile: ProfileInput = ProfileInput()
)

data class ProfileInput(
    val first_name: String = "A",
    val last_name: String = "A",
    val age: Int = 1,
    val gender: String = "M"
)