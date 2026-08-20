package com.example.mynotetaker

data class Note(
    val id: Int = System.currentTimeMillis().toInt(),
    var title: String,
    var content: String
)