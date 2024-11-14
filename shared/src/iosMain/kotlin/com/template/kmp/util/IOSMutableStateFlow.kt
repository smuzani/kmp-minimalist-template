package com.template.kmp.util

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
  initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))