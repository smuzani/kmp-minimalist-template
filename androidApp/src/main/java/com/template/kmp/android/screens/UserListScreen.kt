package com.template.kmp.android.screens

import com.template.kmp.randomuser.ui.RandomUserEvent
import com.template.kmp.android.components.UserCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.kmp.randomuser.ui.RandomUserState

@Composable
fun UserListScreen(
  state: RandomUserState,
  onEvent: (RandomUserEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    when {
      state.isLoading -> {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center)
        )
      }

      state.error != null -> {
        ErrorView(
          message = "Error loading users: ${state.error}",
          onRetry = { onEvent(RandomUserEvent.RetryLoadUsers) },
          modifier = Modifier.align(Alignment.Center)
        )
      }

      state.users.isEmpty() -> {
        Text(
          text = "No users found",
          modifier = Modifier.align(Alignment.Center)
        )
      }

      else -> {
        LazyColumn {
          items(state.users) { user ->
            UserCard(user = user)
          }
        }
      }
    }
  }
}

@Composable
fun ErrorView(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(text = message)
    Button(onClick = onRetry) {
      Text("Retry")
    }
  }
}