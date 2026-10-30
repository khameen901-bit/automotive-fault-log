package com.khameen901.automotivefaultlog.ui.screens

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: FaultViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val result = viewModel.backupDatabase(uri.path ?: "")
                status = if (result) "تم إنشاء نسخة احتياطية بنجاح" else "فشل إنشاء النسخة الاحتياطية"
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val result = viewModel.restoreDatabase(uri)
                status = if (result) "تم استعادة قاعدة البيانات بنجاح" else "فشل استعادة قاعدة البيانات"
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("الإعدادات والنسخ الاحتياطي", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val backupFile = File(context.getExternalFilesDir(null), "fault_log_backup.db")
                    backupLauncher.launch(backupFile.name)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("نسخ احتياطي محلي")
                }
                Button(onClick = { restoreLauncher.launch(arrayOf("application/octet-stream", "application/x-sqlite3")) }, modifier = Modifier.fillMaxWidth()) {
                    Text("استعادة قاعدة البيانات")
                }
                Text(status)
            }
        }
    }
}
