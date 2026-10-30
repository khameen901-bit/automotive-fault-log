package com.khameen901.automotivefaultlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khameen901.automotivefaultlog.data.dao.FaultWithVehicle
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel

@Composable
fun SearchFiltersScreen(
    navController: NavController,
    records: List<FaultWithVehicle>,
    viewModel: FaultViewModel
) {
    var manufacturer by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var engineType by remember { mutableStateOf("") }
    var engineCode by remember { mutableStateOf("") }
    var dtc by remember { mutableStateOf("") }
    var controlModule by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }

    val filtered = records.filter { item ->
        val vehicle = item.vehicle
        (manufacturer.isBlank() || (vehicle?.manufacturer ?: "").contains(manufacturer, ignoreCase = true)) &&
            (model.isBlank() || (vehicle?.model ?: "").contains(model, ignoreCase = true)) &&
            (year.isBlank() || (vehicle?.productionYear?.toString() ?: "").contains(year, ignoreCase = true)) &&
            (engineType.isBlank() || (vehicle?.engineType ?: "").contains(engineType, ignoreCase = true)) &&
            (engineCode.isBlank() || (vehicle?.engineCode ?: "").contains(engineCode, ignoreCase = true)) &&
            (dtc.isBlank() || item.dtcCodes.any { it.code.contains(dtc, ignoreCase = true) }) &&
            (controlModule.isBlank() || item.fault.relatedControlModule.contains(controlModule, ignoreCase = true)) &&
            (symptoms.isBlank() || item.fault.faultSymptoms.contains(symptoms, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("البحث والفلاتر", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = manufacturer, onValueChange = { manufacturer = it }, label = { Text("المصنّع") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("الموديل") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("سنة الإنتاج") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = engineType, onValueChange = { engineType = it }, label = { Text("نوع المحرك") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = engineCode, onValueChange = { engineCode = it }, label = { Text("رمز المحرك") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = dtc, onValueChange = { dtc = it }, label = { Text("DTC") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = controlModule, onValueChange = { controlModule = it }, label = { Text("وحدة التحكم") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = symptoms, onValueChange = { symptoms = it }, label = { Text("أعراض العطل") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { /* filter state already applied */ }, modifier = Modifier.fillMaxWidth()) {
                        Text("تحديث النتائج")
                    }
                }
            }
        }

        item {
            Text("النتائج (${filtered.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(filtered) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("${item.vehicle?.manufacturer ?: "غير معروف"} ${item.vehicle?.model ?: ""}")
                    Text(item.fault.customerComplaint)
                    Text(item.fault.faultSymptoms)
                }
            }
        }
    }
}
