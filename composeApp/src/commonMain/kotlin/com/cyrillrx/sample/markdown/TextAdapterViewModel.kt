package com.cyrillrx.sample.markdown

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TextAdapterViewModel : ViewModel() {

    private val _state: MutableStateFlow<String> = MutableStateFlow("")
    val state: StateFlow<String> = _state.asStateFlow()

    fun onTextChange(text: String) {
        _state.update { text }
    }
}
