package app.room.test.di

import app.room.test.database.LocalDatabase
import app.room.test.database.getRoomDatabase
import app.room.test.ui.MainScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platFormModule = module {
  single<LocalDatabase> {
    getRoomDatabase()
  }
  singleOf(::MainScreenViewModel)
}