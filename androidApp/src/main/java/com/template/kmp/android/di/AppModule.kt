package com.template.kmp.android.di

import com.template.kmp.network.HttpClientFactory
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.network.KtorRandomUserClient
import com.template.kmp.randomuser.network.RandomUserClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideHttpClient(): HttpClient {
    return HttpClientFactory().create()
  }

  @Provides
  @Singleton
  fun provideRandomUserClient(httpClient: HttpClient): RandomUserClient {
    return KtorRandomUserClient(httpClient)
  }

  @Provides
  @Singleton
  fun provideGetRandomUsers(client: RandomUserClient): GetRandomUsers {
    return GetRandomUsers(client)
  }
}