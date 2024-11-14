package com.template.kmp.randomuser.ui

import RandomUserException
import RandomUserState
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.util.Resource
import com.template.kmp.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomUserViewModel(
  private val getRandomUsers: GetRandomUsers,
  private val coroutineScope: CoroutineScope?
) {
  private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

  private val _state = MutableStateFlow(RandomUserState())
  val state = _state.asStateFlow().toCommonStateFlow()

  init {
    loadUsers()
  }

  fun loadUsers(count: Int = 10) {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }

      when (val result = getRandomUsers.execute(count)) {
        is Resource.Success -> {
          _state.update {
            it.copy(
              users = result.data?.results ?: emptyList(),
              isLoading = false,
              error = null
            )
          }
        }

        is Resource.Error -> {
          _state.update {
            it.copy(
              isLoading = false,
              error = (result.throwable as? RandomUserException)?.error
            )
          }
        }
      }
    }
  }
}