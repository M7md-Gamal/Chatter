package com.elkabsh.chatter.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    var _state = MutableStateFlow<SignUpUiState>(SignUpUiState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(email: String, password: String) {
        _state.value = SignUpUiState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.value = SignUpUiState.Success
            }
            .addOnFailureListener {
                _state.value = SignUpUiState.Error
            }

    }
}

sealed class SignUpUiState {
    data object Nothing: SignUpUiState()
    data object Loading: SignUpUiState()
    data object Success : SignUpUiState()
    data object Error : SignUpUiState()
}