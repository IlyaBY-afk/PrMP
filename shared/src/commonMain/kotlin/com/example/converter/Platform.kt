package com.example.converter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform