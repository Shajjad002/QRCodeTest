package com.example.qrcodetest.Presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodetest.Domain.MainInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo:MainInterface
): ViewModel(){

    private  val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    fun startScanning(){
        viewModelScope.launch {
            repo.statrScanning().collect {data ->
                if (!data.isNullOrBlank()){
                    _state.value = _state.value.copy(
                        details = data
                    )
                }
            }
        }
    }

}