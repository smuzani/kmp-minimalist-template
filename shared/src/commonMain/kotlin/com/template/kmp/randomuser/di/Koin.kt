package com.template.kmp.randomuser.di

import com.template.kmp.network.HttpClientFactory
import com.template.kmp.randomuser.network.GetRandomUsers
import com.template.kmp.randomuser.network.KtorRandomUserClient
import com.template.kmp.randomuser.network.RandomUserClient
import com.template.kmp.randomuser.ui.RandomUserViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule())
}

fun commonModule() = module {
    single { HttpClientFactory().create() }
    single<RandomUserClient> { KtorRandomUserClient(get()) }
    single { GetRandomUsers(get()) }
    factory { RandomUserViewModel(get(), null) }
}