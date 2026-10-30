package com.khameen901.automotivefaultlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khameen901.automotivefaultlog.AppScreen
import com.khameen901.automotivefaultlog.data.dao.FaultWithVehicle

@Composable
fun HomeDashboardScreen(
    navController: NavController,
    records: List<FaultWithVehicle>
) {
    val currentCount = records.count { it.fault.faultStatus.equals("Current", ignoreCase = true) }
    val intermittentCount = records.count { it.fault.faultStatus.equals("Intermittent", ignoreCase = true) }
    val historicalCount = records.count { it.fault.faultStatus.equals("Historical", ignoreCase = true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "لوحة التحكم",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardCard(title = "إجمالي الحالات", value = records.size.toString(), modifier = Modifier.weight(1f))
                DashboardCard(title = "حالية", value = currentCount.toString(), modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardCard(title = "متقطعة", value = intermittentCount.toString(), modifier = Modifier.weight(1f))
                DashboardCard(title = "تاريخية", value = historicalCount.toString(), modifier = Modifier.weight(1f))
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickActionButton(
                    text = "إضافة حالة جديدة",
                    icon = Icons.Default.AddCircle,
                    onClick = { navController.navigate("${AppScreen.AddEdit.route}?recordId=0") }
                )
                QuickActionButton(
                    text = "البحث والفلاتر",
                    icon = Icons.Default.Search,
                    onClick = { navController.navigate(AppScreen.Search.route) }
                )
                QuickActionButton(
                    text = "سجلات الأعطال",
                    icon = Icons.Default.BuildCircle,
                    onClick = { navController.navigate(AppScreen.Records.route) }
                )
                QuickActionButton(
                    text = "الإعدادات والنسخ الاحتياطي",
                    icon = Icons.Default.Settings,
                    onClick = { navController.navigate(AppScreen.Settings.route) }
                )
            }
        }

        item {
            Text(
                text = "آخر الحالات",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(records.take(5)) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "${item.vehicle?.manufacturer ?: "غير معروف"} ${item.vehicle?.model ?: ""}".trim(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${item.fault.relatedControlModule} • ${item.fault.faultStatus}")
                    Text(item.fault.customerComplaint)
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text)
            Icon(imageVector = icon, contentDescription = text)
        }
    }
}
