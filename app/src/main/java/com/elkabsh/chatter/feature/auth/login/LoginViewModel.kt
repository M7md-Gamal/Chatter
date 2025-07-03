package com.elkabsh.chatter.feature.auth.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var _state = MutableStateFlow<LoginUiState>(LoginUiState.Nothing)
    val state = _state.asStateFlow()

    fun login(email: String, password: String) {
        _state.value = LoginUiState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.value = LoginUiState.Success
            }
            .addOnFailureListener {
                _state.value = LoginUiState.Error
            }

    }
}

sealed class LoginUiState {
    data object Nothing: LoginUiState()
    data object Loading: LoginUiState()
    data object Success : LoginUiState()
    data object Error : LoginUiState()
}