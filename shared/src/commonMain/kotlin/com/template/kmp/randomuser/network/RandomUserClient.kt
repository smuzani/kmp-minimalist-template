package com.template.kmp.randomuser.network

import com.template.kmp.randomuser.models.RandomUserDto

interface RandomUserClient {
    suspend fun getRandomUsers(count: Int = 10): RandomUserDto
}