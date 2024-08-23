package app.room.test.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun LocalDate.addDays(days: Int) : LocalDate {
  return Instant.fromEpochSeconds(this.getLongFromDate()).plus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).getLocalDate()
}

fun LocalDate.addMonthInDate(months: Int) : LocalDate {
  val instant = Instant.fromEpochSeconds(this.getLongFromDate())
  val updatedInstant = instant.plus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
  return if (this.isLastDayOfMonth())  updatedInstant.getLocalDate().getStartEndDates().second else updatedInstant.getLocalDate()
}


fun LocalDate.getLongFromDate(): Long {
  return this.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds
}

fun newLocalDate(): LocalDate {
  return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun Instant.getLocalDate(): LocalDate {
  return this.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun LocalDate.getStartEndDates(): Pair<LocalDate,LocalDate> {
  return Pair(LocalDate(this.year, this.month, 1), LocalDate(this.year, this.month, this.month.getMonthMaxDays(this.year)))
}

fun Month.getMonthMaxDays(year: Int? = null): Int{
  return when (this) {
    Month.FEBRUARY -> {
      if (year != null && year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
        29
      } else {
        28
      }
    }
    Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
    Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
    else -> 30
  }
}


fun LocalDate.isLastDayOfMonth() : Boolean {
  return this.dayOfMonth == this.month.getMonthMaxDays(this.year)
}

fun String.addOrRemoveMonth(months: Int) : String {
  return Instant.fromEpochSeconds(this.getDateFromMonth().getLongFromDate()).plus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault()).getLocalDate().getMonthStringFromDate()
}

fun String.getDateFromMonth(): LocalDate {
  val (month, year) = this.split("-").map { it.toInt() }
  return LocalDate(year, month, 1)
}

fun LocalDate.getMonthStringFromDate(): String {
  return "${this.monthNumber.toString().padStart(2, '0')}-${this.year}"
}