package com.template.kmp.randomuser.network

import RandomUserDto

interface RandomUserClient {
    suspend fun getRandomUsers(count: Int = 10): RandomUserDto
}