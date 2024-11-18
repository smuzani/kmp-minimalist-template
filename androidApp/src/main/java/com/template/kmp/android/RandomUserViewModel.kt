package com.template.kmp.android

import com.template.kmp.randomuser.ui.RandomUserEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.ui.RandomUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidRandomUserViewModel @Inject constructor(
  private val getRandomUsers: GetRandomUsers
) : ViewModel() {

  private val viewModel by lazy {
    RandomUserViewModel(
      getRandomUsers = getRandomUsers,
      coroutineScope = viewModelScope
    )
  }

  val state = viewModel.state

  fun onEvent(event: RandomUserEvent) {
    viewModel.onEvent(event)
  }
}