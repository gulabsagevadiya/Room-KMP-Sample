package app.room.test

import android.app.Application
import app.room.test.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {
  override fun onCreate() {
    super.onCreate()
    initKoin {
      androidContext(this@MyApp)
    }
  }
}