package com.khameen901.automotivefaultlog.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

object ImageStorage {
    private const val IMAGE_DIR_NAME = "fault_images"

    fun ensureImageDirectory(context: Context): File {
        val directory = File(context.filesDir, IMAGE_DIR_NAME)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    fun copyUriToAppStorage(context: Context, uri: Uri): String? {
        return try {
            val directory = ensureImageDirectory(context)
            val targetFile = File(directory, "fault_${UUID.randomUUID()}.jpg")
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

    fun deleteImageFile(path: String) {
        val file = File(path)
        if (file.exists()) file.delete()
    }

    fun listSavedImages(context: Context): List<String> = ensureImageDirectory(context).listFiles()
        ?.filter { it.exists() && it.isFile }
        ?.map { it.absolutePath }
        ?: emptyList()
}
