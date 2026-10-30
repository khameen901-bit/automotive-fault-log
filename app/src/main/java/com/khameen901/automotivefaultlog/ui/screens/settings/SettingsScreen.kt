package com.khameen901.automotivefaultlog.ui.screens.search

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khameen901.automotivefaultlog.domain.model.FaultFilters
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersScreen(
    navController: NavController,
    viewModel: FaultViewModel
) {
    val uiState by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    var filters by remember { mutableStateOf(FaultFilters()) }

    val results = viewModel.searchRecords(query, filters)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("البحث والفلاتر") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text("بحث عام") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = filters.manufacturer,
                                onValueChange = { filters = filters.copy(manufacturer = it) },
                                label = { Text("المصنع") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = filters.model,
                                onValueChange = { filters = filters.copy(model = it) },
                                label = { Text("الموديل") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = filters.engineType,
                                onValueChange = { filters = filters.copy(engineType = it) },
                                label = { Text("نوع المحرك") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = filters.engineCode,
                                onValueChange = { filters = filters.copy(engineCode = it) },
                                label = { Text("رمز المحرك") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        OutlinedTextField(
                            value = filters.dtcCode,
                            onValueChange = { filters = filters.copy(dtcCode = it) },
                            label = { Text("DTC") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = filters.symptoms,
                            onValueChange = { filters = filters.copy(symptoms = it) },
                            label = { Text("الأعراض") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Text(
                    text = "النتائج (${results.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(results) { record ->
                val vehicle = record.vehicle
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "${vehicle?.manufacturer.orEmpty()} ${vehicle?.model.orEmpty()}", fontWeight = FontWeight.Bold)
                        Text(text = record.faultRecord.customerComplaint.ifBlank { "لا توجد شكوى" })
                        Text(text = "DTC: ${viewModel.state.value.records.firstOrNull { it.faultRecord.id == record.faultRecord.id }?.let { viewModel.getFaultById(record.faultRecord.id) ; "" } ?: ""}")
                    }
                }
            }
        }
    }
}
