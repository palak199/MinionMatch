package com.example.memorychecker.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class MemoryCard(
    val id : Int,
    val imgResId : Int,
    var flipped: MutableState<Boolean> = mutableStateOf(false) // Using MutableState
)
