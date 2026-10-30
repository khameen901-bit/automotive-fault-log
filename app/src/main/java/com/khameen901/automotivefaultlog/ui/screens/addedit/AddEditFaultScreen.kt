package com.khameen901.automotivefaultlog.ui.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: FaultViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var status by remember { mutableStateOf("") }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val result = viewModel.backupDatabase(context, uri)
                status = if (result) "تم إنشاء نسخة احتياطية بنجاح." else "فشل إنشاء النسخة الاحتياطية."
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            pendingUri = uri
            showRestoreDialog = true
        }
    }

    if (showRestoreDialog && pendingUri != null) {
        AlertDialog(
            onDismissRequest = { showRestoreDialog = false },
            title = { Text("استعادة النسخة الاحتياطية") },
            text = { Text("سيتم استبدال البيانات الحالية بالنسخة المختارة. تأكد من أن لديك نسخة احتياطية قبل المتابعة.") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val result = viewModel.restoreDatabase(context, pendingUri!!)
                        status = if (result) "تمت استعادة البيانات بنجاح." else "فشل استعادة البيانات."
                        showRestoreDialog = false
                    }
                }) {
                    Text("استعادة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الإعدادات والنسخ الاحتياطي") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { backupLauncher.launch("automotive_fault_log_backup.zip") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("إنشاء نسخة احتياطية محلية")
                    }

                    Button(
                        onClick = { restoreLauncher.launch(arrayOf("application/zip")) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("استعادة نسخة احتياطية")
                    }

                    Text(
                        text = status.ifBlank { "جاهز للاستخدام" },
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
