package com.example.frontendplantas.views.screen.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun SeleccionadorCalendarioPreview() {
    FrontEndPlantasTheme {
        SeleccionadorCalendario(
            selectedDate = System.currentTimeMillis(),
            onDateSelected = {},
            modifier = Modifier
        )
    }
}

@Composable
fun SeleccionadorCalendario(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var fechaTexto by remember { mutableStateOf(dateFormatter.format(Date(selectedDate))) }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDate
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val selectedMillis = selectedCalendar.timeInMillis
            fechaTexto = dateFormatter.format(Date(selectedMillis))
            onDateSelected(selectedMillis)
        },
        year, month, day
    )

    Box(modifier.fillMaxWidth()) {
        Row(Modifier.align(Alignment.Center)) {
            OutlinedTextField(
                value = fechaTexto,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                },
                label = { Text("Selecciona fecha") }
            )
        }
    }
}
