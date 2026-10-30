package com.khameen901.automotivefaultlog.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun copyUriToAppStorage(context: Context, uri: Uri): String? {
    return try {
        val folder = File(context.filesDir, "fault_images")
        if (!folder.exists()) folder.mkdirs()
        val fileName = "fault_${UUID.randomUUID()}.jpg"
        val targetFile = File(folder, fileName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        targetFile.absolutePath
    } catch (_: Exception) {
        null
    }
}

fun deleteFileIfExists(file: File) {
    if (file.exists()) file.delete()
}
