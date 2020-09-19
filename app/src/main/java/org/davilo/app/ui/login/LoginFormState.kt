package org.davilo.app.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordErrorString: String? = null,
    val isDataValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false
)