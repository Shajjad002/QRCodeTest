package com.example.qrcodetest.Presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen (viewModel: MainViewModel = hiltViewModel()){

    val state = viewModel.state.collectAsState()


}