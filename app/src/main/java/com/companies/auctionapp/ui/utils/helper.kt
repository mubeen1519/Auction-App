package com.companies.auctionapp.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.companies.auctionapp.data.AccountType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Long?.millisToLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this ?: 0).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long?.millisToLocalDate(accountType: AccountType): LocalDate {
    val selectedDate = Instant.ofEpochMilli(this ?: 0).atZone(ZoneId.systemDefault()).toLocalDate()
    return if (accountType == AccountType.Free) {
        selectedDate.plusDays(3)
    } else {
        selectedDate
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertToIsoDateTime(inputDateTime: String): String {
    // Parse the input string into a LocalDateTime object
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val dateTime = LocalDateTime.parse(inputDateTime, formatter)

    // Convert LocalDateTime to ISO date time string
    val isoDateTime = dateTime.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)

    return isoDateTime
}