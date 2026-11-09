package com.azanpro.app.prayer

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.time.LocalDate
import java.util.Date

enum class Prayer { FAJR, SUNRISE, DHUHR, ASR, MAGHRIB, ISHA }

object PrayerCalculator {

    // رابطة العالم الإسلامي
    private val method = CalculationMethod.MUSLIM_WORLD_LEAGUE

    fun timesFor(lat: Double, lng: Double, date: LocalDate): PrayerTimes {
        val coordinates = Coordinates(lat, lng)
        val components = DateComponents(date.year, date.monthValue, date.dayOfMonth)
        return PrayerTimes(coordinates, components, method.parameters)
    }

    /** الصلاة القادمة بعد لحظة معينة، مع الانتقال لليوم التالي عند الحاجة. */
    fun nextPrayer(lat: Double, lng: Double, now: Date = Date()): Pair<Prayer, Date> {
        val today = timesFor(lat, lng, java.time.LocalDate.now())
        val next = today.nextPrayer(now)
        if (next != com.batoulapps.adhan.Prayer.NONE) {
            return next.toApp() to today.timeForPrayer(next)!!
        }
        val tomorrow = timesFor(lat, lng, java.time.LocalDate.now().plusDays(1))
        return Prayer.FAJR to tomorrow.fajr
    }

    private fun com.batoulapps.adhan.Prayer.toApp(): Prayer = when (this) {
        com.batoulapps.adhan.Prayer.FAJR -> Prayer.FAJR
        com.batoulapps.adhan.Prayer.SUNRISE -> Prayer.SUNRISE
        com.batoulapps.adhan.Prayer.DHUHR -> Prayer.DHUHR
        com.batoulapps.adhan.Prayer.ASR -> Prayer.ASR
        com.batoulapps.adhan.Prayer.MAGHRIB -> Prayer.MAGHRIB
        com.batoulapps.adhan.Prayer.ISHA -> Prayer.ISHA
        else -> Prayer.FAJR
    }
}
