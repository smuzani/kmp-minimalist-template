package com.template.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform