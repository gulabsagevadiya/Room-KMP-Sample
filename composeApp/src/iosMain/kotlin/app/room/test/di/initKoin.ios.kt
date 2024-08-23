package app.room.test.di

import app.room.test.database.LocalDatabase
import app.room.test.database.getAppDatabase
import app.room.test.ui.MainScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platFormModule = module {
  single<LocalDatabase> {
    getAppDatabase()
  }
  singleOf(::MainScreenViewModel)
}