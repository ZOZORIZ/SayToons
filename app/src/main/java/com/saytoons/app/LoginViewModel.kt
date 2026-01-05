package com.saytoons.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saytoons.app.data.ParentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = ParentRepository()

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading
            val result = repository.loginParent(email, pass)
            _loginState.value = if (result.isSuccess) LoginState.Success else LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            _isLoading.value = false
        }
    }

    fun googleLogin(idToken: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.signInWithGoogle(idToken)
            _isLoading.value = false
            if (result.isSuccess) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Google Sign In Failed")
            }
        }
    }



    fun signup(
        email: String,
        pass: String,
        parentName: String,
        kidNames: List<String>,
        selectedAgeRange: String,
        selectedGender: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading


            val result = repository.createParentAccount(
                email,
                pass,
                parentName,
                kidNames,
                selectedAgeRange,
                selectedGender
            )

            _loginState.value = if (result.isSuccess) LoginState.Success else LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            _isLoading.value = false
        }
    }
}