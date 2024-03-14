package com.example.simonkye_sharedpreferences

import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ViewModel"

class SharedPreferencesViewModel : ViewModel() {
    private val preferencesRepository = PreferencesRepository.get()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState(image = 0))
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedStr ->
                try {
                    _uiState.update { oldState ->
                        oldState.copy(
                            word = storedStr
                        )
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Failed to fetch gallery items", ex)
                }
            }
        }

        viewModelScope.launch {
            preferencesRepository.imgResId.collectLatest { resId ->
                try {
                    _uiState.update { oldState ->
                        oldState.copy(image = resId)
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Failed to fetch image resource ID", ex)
                }
            }
        }


    }
    fun setQuery(query: String) {
        viewModelScope.launch { preferencesRepository.setStoredQuery(query) }
    }

    fun setImgResId(resId: Int) {
        viewModelScope.launch {
            preferencesRepository.setImgResId(resId)
        }
    }
}

data class UiState(
    val image: Int,
    val word: String = ""
)