package com.khameen901.automotivefaultlog.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun copyUriToAppStorage(context: Context, uri: Uri): String? {
    return try {
        val folder = File(context.filesDir, "fault_images")
        if (!folder.exists()) folder.mkdirs()
        val target = File(folder, "fault_${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            target.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        target.absolutePath
    } catch (_: Exception) {
        null
    }
}

fun deleteFileIfExists(file: File) {
    if (file.exists()) {
        file.delete()
    }
}
