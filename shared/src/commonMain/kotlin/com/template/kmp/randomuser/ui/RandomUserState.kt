package com.template.kmp.randomuser.ui

import com.template.kmp.randomuser.network.RandomUserError
import com.template.kmp.randomuser.models.UserDto

data class RandomUserState(
    val users: List<UserDto> = emptyList(),
    val filteredUsers: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: RandomUserError? = null
)