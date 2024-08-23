package app.room.test.di

import app.room.test.repository.SampleDataRepo
import app.room.test.repository.SampleDataRepoImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration?= null) {
  startKoin {
    config?.invoke(this)
    modules(platFormModule, sharedModule)
  }
}

expect val platFormModule: Module

val sharedModule = module {
  singleOf(::SampleDataRepoImpl).bind<SampleDataRepo>()
}