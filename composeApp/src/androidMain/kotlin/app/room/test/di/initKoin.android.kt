package app.room.test.di

import app.room.test.database.LocalDatabase
import app.room.test.database.getAppDatabase
import app.room.test.ui.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

actual val platFormModule = module {
  single<LocalDatabase> {
    getAppDatabase(get())
  }
  viewModelOf(::MainScreenViewModel)
}