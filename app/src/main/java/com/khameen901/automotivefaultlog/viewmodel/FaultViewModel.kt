package com.khameen901.automotivefaultlog.utils

import android.content.Context
import android.net.Uri
import com.khameen901.automotivefaultlog.data.local.database.FaultDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object BackupRestore {
    private const val DB_NAME = FaultDatabase.DB_NAME
    private const val IMAGE_DIR = "fault_images"

    fun backupDatabase(context: Context, outputUri: Uri): Boolean {
        return try {
            val outputStream = context.contentResolver.openOutputStream(outputUri, "w") ?: return false
            val dbFile = context.getDatabasePath(DB_NAME)
            val imageDirectory = File(context.filesDir, IMAGE_DIR)
            val imageFiles = imageDirectory.listFiles()?.filter { it.isFile }?.sortedBy { it.name } ?: emptyList()

            ZipOutputStream(outputStream).use { zipOut ->
                zipOut.putNextEntry(ZipEntry("metadata.json"))
                val metadata = """{"database":"$DB_NAME","image_count":${imageFiles.size},"version":1}"""
                zipOut.write(metadata.toByteArray())
                zipOut.closeEntry()

                zipOut.putNextEntry(ZipEntry("database/$DB_NAME"))
                FileInputStream(dbFile).use { input ->
                    input.copyTo(zipOut)
                }
                zipOut.closeEntry()

                imageFiles.forEach { imageFile ->
                    zipOut.putNextEntry(ZipEntry("images/${imageFile.name}"))
                    imageFile.inputStream().use { input ->
                        input.copyTo(zipOut)
                    }
                    zipOut.closeEntry()
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun restoreDatabase(context: Context, inputUri: Uri): Boolean {
        return try {
            val dbFile = context.getDatabasePath(DB_NAME)
            val imageDirectory = File(context.filesDir, IMAGE_DIR)
            if (!imageDirectory.exists()) imageDirectory.mkdirs()

            val tempDir = File(context.cacheDir, "restore_temp")
            tempDir.mkdirs()
            val tempDb = File(tempDir, DB_NAME)

            context.contentResolver.openInputStream(inputUri)?.use { stream ->
                ZipInputStream(stream).use { zipIn ->
                    var entry = zipIn.nextEntry
                    while (entry != null) {
                        when {
                            entry.name.endsWith("/$DB_NAME") || entry.name == DB_NAME -> {
                                tempDb.outputStream().use { output -> zipIn.copyTo(output) }
                            }
                            entry.name.startsWith("images/") -> {
                                val fileName = entry.name.substringAfterLast('/')
                                val targetFile = File(imageDirectory, fileName)
                                targetFile.outputStream().use { output -> zipIn.copyTo(output) }
                            }
                        }
                        zipIn.closeEntry()
                        entry = zipIn.nextEntry
                    }
                }
            }

            if (tempDb.exists()) {
                val backupDb = File(dbFile.absolutePath)
                if (backupDb.exists()) {
                    backupDb.delete()
                }
                FileOutputStream(backupDb).use { output -> FileInputStream(tempDb).copyTo(output) }
                FaultDatabase.resetInstance()
            }

            true
        } catch (_: Exception) {
            false
        }
    }
}
