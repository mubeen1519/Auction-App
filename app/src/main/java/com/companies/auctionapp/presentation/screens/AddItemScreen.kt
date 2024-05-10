package com.companies.auctionapp.presentation.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.R
import com.companies.auctionapp.presentation.components.TimePickerDialog
import com.companies.auctionapp.presentation.viewModel.AddViewModel
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemScreen(
    navigate: (String) -> Unit,
    viewModel: AddViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val data = viewModel.data

    val context = LocalContext.current
    var startDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val startDateAndTime = viewModel.startDateAndTime.value
    var endDateAndTime = viewModel.endDateAndTime.value
    val datePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()
    val endTimePickerState = rememberTimePickerState()
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showDialogEnd = rememberSaveable { mutableStateOf(false) }
    val timePickerStateVertical = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    var showTimePickerEnd by remember { mutableStateOf(false) }

    val defaultEndDate: Calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 3) // Default end time for Free accounts: 3 days after start time
    }

    val minEndDate: Calendar = Calendar.getInstance().apply {
        add(Calendar.HOUR_OF_DAY, 12) // Minimum end time for Gold and Platinum users: 12 hours after start time
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = data.value.name,
            onValueChange = viewModel::onNameChanged,
            label = { Text(stringResource(id = R.string.Item_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = data.value.description,
            onValueChange = viewModel::onDescriptionChanged,
            label = { Text(stringResource(id = R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = data.value.startingPrice.toString(),
            onValueChange = viewModel::onStartPriceChanged,
            label = { Text(stringResource(id = R.string.starting_price)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .height(50.dp)
                .padding(10.dp)
                .clickable { showDialog.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (startDateAndTime.isEmpty()) {
                Text(text = stringResource(id = R.string.start_date_and_time), modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(id = R.string.start_date_and_time))
            } else {

                Text(text = startDateAndTime)
            }
            if (showDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { showDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            val selectedTime = LocalTime.of(timePickerStateVertical.hour, timePickerStateVertical.minute)
                            if (selectedDateMillis != null) {
                                viewModel.onStartDateTimeSelected(selectedDateMillis, selectedTime)
                            }
                            showDialog.value = false
                            showTimePicker = true
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState, title = {
                        Text(text = stringResource(id = R.string.start_date))
                    })
                }
            }

            if (showTimePicker) {
                TimePickerDialog(
                    onCancel = { showTimePicker = false },
                    onConfirm = {
                        showTimePicker = false
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerStateVertical.hour)
                        calendar.set(Calendar.MINUTE, timePickerStateVertical.minute)
                    },
                ) {
                    TimePicker(
                        state = timePickerStateVertical,
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .height(50.dp)
                .padding(10.dp)
                .clickable { showDialogEnd.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (endDateAndTime.isEmpty()) {
                Text(text = stringResource(id = R.string.end_date_and_time), modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(id = R.string.end_date_and_time))
            } else {
                Text(text = endDateAndTime)
            }
            if (showDialogEnd.value) {
                DatePickerDialog(
                    onDismissRequest = { showDialogEnd.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val formattedDateAndTime =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(endDatePickerState.selectedDateMillis)
                            endDate = formattedDateAndTime
                            endDateAndTime = endDate
                            showDialogEnd.value = false
                            showTimePickerEnd = true
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialogEnd.value = false }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(state = endDatePickerState, title = {
                        Text(text = stringResource(id = R.string.end_date))
                    })
                }
            }

            if (showTimePickerEnd) {
                TimePickerDialog(
                    onCancel = { showTimePickerEnd = false },
                    onConfirm = {
                        showTimePickerEnd = false
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                        calendar.set(Calendar.MINUTE, endTimePickerState.minute)
                        val timeInMillis = calendar.time.time
                        val formattedTime =
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(timeInMillis)
                        endTime = formattedTime
                        endDateAndTime = "$endDate$endTime"
                    },
                ) {
                    TimePicker(
                        state = endTimePickerState,
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = data.value.category,
            onValueChange = viewModel::category,
            label = { Text(stringResource(id = R.string.category)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addAuctionItem(navigate,context)
            Log.d("TAG", "AddNewItemScreen: ${data.value}")
        }
        ) {
            Text("Submit")
        }
    }
}
