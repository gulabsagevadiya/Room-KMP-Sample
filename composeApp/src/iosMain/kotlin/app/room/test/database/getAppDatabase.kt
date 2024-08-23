package app.room.test.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getAppDatabase(): LocalDatabase {
  return Room.databaseBuilder<LocalDatabase>(
    name = documentDirectory() + "/my_room.db",
    factory = { LocalDatabase::class.instantiateImpl() })
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
}

//fun getAppDatabase(): LocalDatabase {
//  val dbFilePath = documentDirectory() + "/my_room.db"
//  return Room.databaseBuilder<LocalDatabase>(name = dbFilePath)
//    .setDriver(BundledSQLiteDriver())
//    .setQueryCoroutineContext(Dispatchers.IO)
//    .build()
//}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
  val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
    directory = NSDocumentDirectory,
    inDomain = NSUserDomainMask,
    appropriateForURL = null,
    create = false,
    error = null,
  )
  return requireNotNull(documentDirectory?.path)
}