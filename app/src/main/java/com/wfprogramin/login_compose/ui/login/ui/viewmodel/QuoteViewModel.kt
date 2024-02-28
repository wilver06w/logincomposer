package com.wfprogramin.login_compose.ui.login.ui.viewmodel


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wfprogramin.login_compose.ui.login.ui.LoginUIState
import com.wfprogramin.login_compose.util.BasicValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _passWord = MutableLiveData<String>()
    val password: LiveData<String> = _passWord
    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    val isLoading = MutableLiveData<Boolean>()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Loading)
    val uiState: StateFlow<LoginUIState> = _uiState


    fun sendMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _passWord.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 6

    suspend fun onLoginSuccess() {
        isLoading.postValue(true)
        _uiState.value = LoginUIState.Loading
        delay(4000)

        isLoading.postValue(false)
        _uiState.value = LoginUIState.Success
    }

    suspend fun onLoginError() {
        isLoading.postValue(true)
        _uiState.value = LoginUIState.Loading
        delay(4000)

        isLoading.postValue(false)
        _uiState.value = LoginUIState.Error(BasicValues.failedRequest)
    }
}