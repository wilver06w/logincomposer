package com.wfprogramin.login_compose.ui.login.ui

sealed class LoginUIState {
    data object Loading : LoginUIState()
    data object Success : LoginUIState()
    data class Error(val msg: String) : LoginUIState()
}