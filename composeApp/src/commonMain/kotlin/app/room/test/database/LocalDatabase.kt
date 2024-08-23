package app.room.test.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Database(
  entities =
  [
    SampleData::class
  ], version = 1, exportSchema = true
)
@TypeConverters(RoomTypeConvertor::class)
//@ConstructedBy(LocalDatabaseCtor::class)
abstract class LocalDatabase : RoomDatabase(), DB{
  abstract fun localDao(): LocalDao
  override fun clearAllTables() {
    super.clearAllTables()
  }
}

@Dao
interface LocalDao {

  @Query("SELECT * FROM SampleData")
  fun getAll(): Flow<List<SampleData>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(sampleData: List<SampleData>)

  @Query("SELECT date FROM SampleData WHERE strftime('%m-%Y', date/1000, 'unixepoch') LIKE :month")
  fun getMonthWiseDates(month: String): Flow<List<LocalDate>>

}

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
  fun clearAllTables() {}
}


//ERROR: file:///Users/Demo/Desktop/Multiplatform/RoomSample/composeApp/build/generated/ksp/metadata/commonMain/kotlin/app/room/test/database/LocalDatabaseCtor.kt:5:22 'actual object LocalDatabaseCtor : RoomDatabaseConstructor<LocalDatabase>' has no corresponding expected declaration
//@Suppress("NO_ACTUAL_FOR_EXPECT")
//expect object LocalDatabaseCtor : RoomDatabaseConstructor<LocalDatabase>

@Entity(tableName = "SampleData")
data class SampleData(
  @PrimaryKey(autoGenerate = true)
  val uid: Int? = null,
  val date: LocalDate,
)

class RoomTypeConvertor  {

  @TypeConverter
  fun dateToLong(date: LocalDate) : Long {
    return date.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds
  }

  @TypeConverter
  fun longToDate(long: Long) : LocalDate {
    return Instant.fromEpochSeconds(long).toLocalDateTime(TimeZone.currentSystemDefault()).date
  }

}