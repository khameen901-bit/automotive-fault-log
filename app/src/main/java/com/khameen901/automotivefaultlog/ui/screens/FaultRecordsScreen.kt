package com.khameen901.automotivefaultlog.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khameen901.automotivefaultlog.data.dao.FaultWithVehicle
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel
import kotlinx.coroutines.launch

@Composable
fun AddEditFaultScreen(
    navController: NavController,
    viewModel: FaultViewModel,
    selectedRecord: FaultWithVehicle?
) {
    val scope = rememberCoroutineScope()
    val initialVehicle = selectedRecord?.vehicle ?: VehicleInfo(
        manufacturer = "",
        model = "",
        productionYear = 2024,
        fuelType = "Petrol",
        engineType = "",
        engineDisplacement = "",
        numberOfCylinders = 4,
        engineCode = "",
        transmissionType = "Automatic",
        drivetrain = "FWD",
        vin = ""
    )
    val initialRecord = selectedRecord?.fault ?: FaultRecord(
        vehicleId = 0L,
        customerComplaint = "",
        faultSymptoms = "",
        relatedControlModule = "",
        faultStatus = "Current"
    )

    var manufacturer by remember { mutableStateOf(initialVehicle.manufacturer) }
    var model by remember { mutableStateOf(initialVehicle.model) }
    var productionYear by remember { mutableStateOf(initialVehicle.productionYear.toString()) }
    var fuelType by remember { mutableStateOf(initialVehicle.fuelType) }
    var engineType by remember { mutableStateOf(initialVehicle.engineType) }
    var engineDisplacement by remember { mutableStateOf(initialVehicle.engineDisplacement) }
    var numberOfCylinders by remember { mutableStateOf(initialVehicle.numberOfCylinders.toString()) }
    var engineCode by remember { mutableStateOf(initialVehicle.engineCode) }
    var transmissionType by remember { mutableStateOf(initialVehicle.transmissionType) }
    var drivetrain by remember { mutableStateOf(initialVehicle.drivetrain) }
    var vin by remember { mutableStateOf(initialVehicle.vin ?: "") }

    var customerComplaint by remember { mutableStateOf(initialRecord.customerComplaint) }
    var faultSymptoms by remember { mutableStateOf(initialRecord.faultSymptoms) }
    var relatedControlModule by remember { mutableStateOf(initialRecord.relatedControlModule) }
    var faultStatus by remember { mutableStateOf(initialRecord.faultStatus) }
    var freezeFrameData by remember { mutableStateOf(initialRecord.freezeFrameData ?: "") }
    var diagnosticTestsPerformed by remember { mutableStateOf(initialRecord.diagnosticTestsPerformed ?: "") }
    var electricalMeasurements by remember { mutableStateOf(initialRecord.electricalMeasurements ?: "") }
    var testedComponents by remember { mutableStateOf(initialRecord.testedComponents ?: "") }
    var suspectedCause by remember { mutableStateOf(initialRecord.suspectedCause ?: "") }
    var confirmedRootCause by remember { mutableStateOf(initialRecord.confirmedRootCause ?: "") }
    var repairProcedure by remember { mutableStateOf(initialRecord.repairProcedure ?: "") }
    var finalVerificationResult by remember { mutableStateOf(initialRecord.finalVerificationResult ?: "") }
    var notes by remember { mutableStateOf(initialRecord.notes ?: "") }
    var dtcText by remember { mutableStateOf(selectedRecord?.dtcCodes?.joinToString(", ") { it.code } ?: "") }
    var diagnosticText by remember { mutableStateOf(selectedRecord?.tests?.joinToString("\n") { "${it.testName}: ${it.result}" } ?: "") }

    val imagePaths = remember(selectedRecord?.fault?.id) {
        mutableStateListOf<String>().apply {
            addAll(selectedRecord?.images?.map { it.imagePath } ?: emptyList())
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            scope.launch {
                viewModel.savePhotoFromUri(uri)?.let { savedPath ->
                    if (!imagePaths.contains(savedPath)) imagePaths.add(savedPath)
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = if (selectedRecord == null) "إضافة حالة جديدة" else "تعديل الحالة",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("معلومات المركبة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = manufacturer, onValueChange = { manufacturer = it }, label = { Text("المصنّع") }, modifier = Modifier.fillMaxWidth())
                        }
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("الموديل") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = productionYear, onValueChange = { productionYear = it }, label = { Text("سنة الإنتاج") }, modifier = Modifier.fillMaxWidth())
                        }
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = fuelType, onValueChange = { fuelType = it }, label = { Text("نوع الوقود") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = engineType, onValueChange = { engineType = it }, label = { Text("نوع المحرك") }, modifier = Modifier.fillMaxWidth())
                        }
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = engineDisplacement, onValueChange = { engineDisplacement = it }, label = { Text("السعة") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = numberOfCylinders, onValueChange = { numberOfCylinders = it }, label = { Text("عدد الأسطوانات") }, modifier = Modifier.fillMaxWidth())
                        }
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = engineCode, onValueChange = { engineCode = it }, label = { Text("رمز المحرك") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = transmissionType, onValueChange = { transmissionType = it }, label = { Text("نوع ناقل الحركة") }, modifier = Modifier.fillMaxWidth())
                        }
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(value = drivetrain, onValueChange = { drivetrain = it }, label = { Text("نظام الدفع") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    OutlinedTextField(value = vin, onValueChange = { vin = it }, label = { Text("VIN (اختياري)") }, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("معلومات العطل", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = customerComplaint, onValueChange = { customerComplaint = it }, label = { Text("شكوى العميل") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = faultSymptoms, onValueChange = { faultSymptoms = it }, label = { Text("أعراض العطل") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = dtcText, onValueChange = { dtcText = it }, label = { Text("رموز DTC (مفصولة بفواصل)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = relatedControlModule, onValueChange = { relatedControlModule = it }, label = { Text("الوحدة المتحكمة") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = faultStatus, onValueChange = { faultStatus = it }, label = { Text("حالة العطل") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = freezeFrameData, onValueChange = { freezeFrameData = it }, label = { Text("Freeze Frame (اختياري)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = diagnosticTestsPerformed, onValueChange = { diagnosticTestsPerformed = it }, label = { Text("اختبارات التشخيص") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = electricalMeasurements, onValueChange = { electricalMeasurements = it }, label = { Text("القياسات الكهربائية") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = testedComponents, onValueChange = { testedComponents = it }, label = { Text("المكونات المختبرة") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = suspectedCause, onValueChange = { suspectedCause = it }, label = { Text("السبب المحتمل") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = confirmedRootCause, onValueChange = { confirmedRootCause = it }, label = { Text("سبب الجذر المؤكد") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = repairProcedure, onValueChange = { repairProcedure = it }, label = { Text("إجراءات الإصلاح") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = finalVerificationResult, onValueChange = { finalVerificationResult = it }, label = { Text("نتيجة التحقق النهائية") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("ملاحظات") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = diagnosticText, onValueChange = { diagnosticText = it }, label = { Text("اختبارات تفصيلية (اسم: النتيجة)") }, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("الصور المرفقة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Button(onClick = { photoPickerLauncher.launch("image/*") }) {
                            Text("إضافة صورة")
                        }
                    }

                    if (imagePaths.isNotEmpty()) {
                        imagePaths.forEach { path ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(path.substringAfterLast('/'))
                                Button(onClick = { imagePaths.remove(path) }) {
                                    Text("حذف")
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    val vehicle = VehicleInfo(
                        id = initialVehicle.id,
                        manufacturer = manufacturer.trim(),
                        model = model.trim(),
                        productionYear = productionYear.toIntOrNull() ?: 2024,
                        fuelType = fuelType.trim(),
                        engineType = engineType.trim(),
                        engineDisplacement = engineDisplacement.trim(),
                        numberOfCylinders = numberOfCylinders.toIntOrNull() ?: 4,
                        engineCode = engineCode.trim(),
                        transmissionType = transmissionType.trim(),
                        drivetrain = drivetrain.trim(),
                        vin = vin.trim().ifBlank { null }
                    )
                    val fault = FaultRecord(
                        id = initialRecord.id,
                        vehicleId = initialRecord.vehicleId,
                        customerComplaint = customerComplaint.trim(),
                        faultSymptoms = faultSymptoms.trim(),
                        relatedControlModule = relatedControlModule.trim(),
                        faultStatus = faultStatus.trim(),
                        freezeFrameData = freezeFrameData.trim().ifBlank { null },
                        diagnosticTestsPerformed = diagnosticTestsPerformed.trim().ifBlank { null },
                        electricalMeasurements = electricalMeasurements.trim().ifBlank { null },
                        testedComponents = testedComponents.trim().ifBlank { null },
                        suspectedCause = suspectedCause.trim().ifBlank { null },
                        confirmedRootCause = confirmedRootCause.trim().ifBlank { null },
                        repairProcedure = repairProcedure.trim().ifBlank { null },
                        finalVerificationResult = finalVerificationResult.trim().ifBlank { null },
                        notes = notes.trim().ifBlank { null },
                        createdAt = initialRecord.createdAt,
                        updatedAt = System.currentTimeMillis()
                    )
                    val dtcCodes = dtcText.split(',').map { it.trim() }.filter { it.isNotEmpty() }
                    val tests = diagnosticText.split('\n').mapNotNull { line ->
                        val parts = line.split(":", limit = 2)
                        if (parts.size == 2) DiagnosticTest(testName = parts[0].trim(), result = parts[1].trim()) else null
                    }
                    scope.launch {
                        viewModel.saveFault(vehicle, fault, dtcCodes, tests, imagePaths.toList())
                    }
                    navController.popBackStack()
                }, modifier = Modifier.weight(1f)) {
                    Text("حفظ")
                }
                Button(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                    Text("إلغاء")
                }
            }
        }
    }
}
