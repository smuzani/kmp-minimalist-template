package com.template.kmp.randomuser.ui

sealed class RandomUserEvent {
  data class LoadUsers(val count: Int = 10) : RandomUserEvent()
  object RetryLoadUsers : RandomUserEvent()
  object OnErrorSeen : RandomUserEvent()
  data class FilterByGender(val gender: String?) : RandomUserEvent()
  data class SearchUsers(val query: String) : RandomUserEvent()
  object ClearSearch : RandomUserEvent()
  data class SortUsers(val sortBy: SortOption) : RandomUserEvent()
  object RefreshUsers : RandomUserEvent()
}

enum class SortOption {
  NAME_ASC,
  NAME_DESC,
  AGE_ASC,
  AGE_DESC
} 