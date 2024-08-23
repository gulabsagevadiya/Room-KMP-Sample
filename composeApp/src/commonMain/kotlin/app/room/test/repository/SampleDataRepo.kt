package app.room.test.repository

import app.room.test.database.LocalDatabase
import app.room.test.database.SampleData
import app.room.test.utils.addDays
import app.room.test.utils.addMonthInDate
import app.room.test.utils.newLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

interface SampleDataRepo {

  fun getMonthWiseDates(month: String): Flow<List<LocalDate>>
  fun getAllDates(): Flow<List<LocalDate>>

}

class SampleDataRepoImpl(localDatabase: LocalDatabase) : SampleDataRepo {
  private val localDao = localDatabase.localDao()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      val getData = localDao.getAll().first()
      if (getData.isEmpty()){
        val today = newLocalDate().addMonthInDate(-6)
        val fakeDateList = arrayListOf<SampleData>().apply {
          repeat(365) {
            add(SampleData(date = today.addDays(it)))
          }
        }
        localDao.insert(fakeDateList)
      }
    }
  }

  override fun getMonthWiseDates(month: String): Flow<List<LocalDate>> {
    return localDao.getMonthWiseDates(month)
  }

  override fun getAllDates(): Flow<List<LocalDate>> {
    return localDao.getAll().map { it.map { it.date } }
  }

}