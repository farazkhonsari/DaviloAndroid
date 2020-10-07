package org.davilo.app.model

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordErrorString: String? = null,
    val isDataValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val isVerified: Boolean = false,
    val email: String? = null,
    val password: String? = null
)