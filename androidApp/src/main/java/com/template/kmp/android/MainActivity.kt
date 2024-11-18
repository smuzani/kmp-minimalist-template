package com.template.kmp.android

import com.template.kmp.android.screens.Routes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.kmp.android.screens.UserListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          AppRoot()
        }
      }
    }
  }
}

@Composable
fun AppRoot() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = Routes.USER_LIST
  ) {
    composable(route = Routes.USER_LIST) {
      val viewModel = hiltViewModel<AndroidRandomUserViewModel>()
      val state by viewModel.state.collectAsState()
      UserListScreen(
        state = state,
        onEvent = viewModel::onEvent
      )
    }
  }
}
