package com.template.kmp.randomuser.di

import cocoapods.XenditFingerprintSDK.FingerprintSDK
import com.template.kmp.network.HttpClientFactory
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.network.KtorRandomUserClient
import com.template.kmp.randomuser.network.RandomUserClient
import kotlinx.cinterop.ExperimentalForeignApi

interface AppModule {
  val client: RandomUserClient
  val useCase: GetRandomUsers

  fun testSDK(apiKey: String)
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

  @OptIn(ExperimentalForeignApi::class)
  override fun testSDK(apiKey: String) {
    val fingerprintSDK = FingerprintSDK()
    fingerprintSDK.initSDKWithApiKey(apiKey)
    fingerprintSDK.scanWithEvent_name(
      event_name = "asd",
      event_id = "asdasd",
      completion = { response, errorMsg -> println("message  $errorMsg") }
    )
  }
}