package com.template.kmp.randomuser.ui

import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.network.RandomUserException
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
          val users = result.data?.results ?: emptyList()
          _state.update {
            it.copy(
              users = users,
              filteredUsers = users,
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

  fun onEvent(event: RandomUserEvent) {
    when (event) {
      is RandomUserEvent.LoadUsers -> {
        loadUsers(event.count)
      }

      is RandomUserEvent.RetryLoadUsers -> {
        loadUsers()
      }

      is RandomUserEvent.OnErrorSeen -> {
        _state.update { it.copy(error = null) }
      }

      is RandomUserEvent.FilterByGender -> {
        _state.update { currentState ->
          currentState.copy(
            filteredUsers = when (event.gender) {
              null -> currentState.users
              else -> currentState.users.filter { it.gender == event.gender }
            }
          )
        }
      }

      is RandomUserEvent.SearchUsers -> {
        _state.update { currentState ->
          currentState.copy(
            filteredUsers = currentState.users.filter { user ->
              user.name.first.contains(event.query, ignoreCase = true) ||
                  user.name.last.contains(event.query, ignoreCase = true) ||
                  user.email.contains(event.query, ignoreCase = true)
            }
          )
        }
      }

      RandomUserEvent.ClearSearch -> {
        _state.update { it.copy(filteredUsers = it.users) }
      }

      is RandomUserEvent.SortUsers -> {
        _state.update { currentState ->
          currentState.copy(
            filteredUsers = when (event.sortBy) {
              SortOption.NAME_ASC -> currentState.filteredUsers.sortedBy { "${it.name.first} ${it.name.last}" }
              SortOption.NAME_DESC -> currentState.filteredUsers.sortedByDescending { "${it.name.first} ${it.name.last}" }
              SortOption.AGE_ASC -> currentState.filteredUsers.sortedBy { it.dob.age }
              SortOption.AGE_DESC -> currentState.filteredUsers.sortedByDescending { it.dob.age }
            }
          )
        }
      }

      RandomUserEvent.RefreshUsers -> {
        viewModelScope.launch {
          _state.update { it.copy(isLoading = true) }
          loadUsers()
        }
      }

      else -> {
        println("Unhandled com.template.kmp.randomuser.ui.RandomUserEvent: $event")
      }
    }
  }
}