package com.example.qrcodetest.Domain

import kotlinx.coroutines.flow.Flow

interface MainInterface {
    fun statrScanning() : Flow<String?>
}