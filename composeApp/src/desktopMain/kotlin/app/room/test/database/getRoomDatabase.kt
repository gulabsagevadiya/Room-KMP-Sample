package app.room.test.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

fun getRoomDatabase(): LocalDatabase {
  val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
  return Room.databaseBuilder<LocalDatabase>(name = dbFile.absolutePath)
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
}
