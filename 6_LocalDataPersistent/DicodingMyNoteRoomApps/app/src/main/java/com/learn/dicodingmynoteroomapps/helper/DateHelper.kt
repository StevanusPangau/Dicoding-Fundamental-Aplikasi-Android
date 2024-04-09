package com.learn.dicodingmynoteroomapps.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
Kelas ini berfungsi untuk mendapatkan waktu seperti tanggal, bulan, tahun, dan jam
 */

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}