package com.example.tikitrendingproject.model

data class ResponseObject<T>(
    val status: Int,
    val data: T
)