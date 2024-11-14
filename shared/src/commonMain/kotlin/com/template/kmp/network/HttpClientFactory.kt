package com.template.kmp.network

import io.ktor.client.*

expect class HttpClientFactory {
    fun create(): HttpClient
}