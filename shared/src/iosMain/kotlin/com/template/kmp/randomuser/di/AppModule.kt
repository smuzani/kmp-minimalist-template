package com.template.kmp.randomuser.di

import com.template.kmp.network.HttpClientFactory
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.network.KtorRandomUserClient
import com.template.kmp.randomuser.network.RandomUserClient

interface AppModule {
  val client: RandomUserClient
  val useCase: GetRandomUsers
}

class AppModuleImpl : AppModule {
  override val client: RandomUserClient by lazy {
    KtorRandomUserClient(
      HttpClientFactory().create()
    )
  }

  override val useCase: GetRandomUsers by lazy {
    GetRandomUsers(client)
  }
}