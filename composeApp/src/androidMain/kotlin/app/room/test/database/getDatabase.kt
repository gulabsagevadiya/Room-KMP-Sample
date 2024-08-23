package app.room.test.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun getAppDatabase(context: Context): LocalDatabase {
  val dbFile = context.getDatabasePath("LocalMain.db")
  return Room.databaseBuilder<LocalDatabase>(name = dbFile.absolutePath, context = context)
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
}