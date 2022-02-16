package com.example.statemanagementtutorialyt

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _textFieldState = mutableStateOf<TextFieldState>(TextFieldState())
    val textFieldState = _textFieldState

    val uiEvent = Channel<UiEvent>()


    fun onTextFieldValueChange(value: String) {
        _textFieldState.value = TextFieldState(
            textFieldString = value,
            isError = value.length < 5,
            placeholder = if (value.length < 5) "Username should have at least 5 characters" else ""
        )
    }

    fun onSaveButtonClicked() {
        if(_textFieldState.value.textFieldString.length < 5){
            showEvent(UiEvent.ShowSnackbar("Username length should be at least 5 characters"))
        }else{
            showEvent(UiEvent.ShowSnackbar("Saved successfully"))
        }
    }

    fun showEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                is UiEvent.ShowSnackbar -> uiEvent.send(event)
            }
        }
    }
}

sealed class UiEvent() {
    class ShowSnackbar(val message: String) : UiEvent()
}
