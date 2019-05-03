package com.cicuro.core.utils

import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import org.joda.time.DateTime
import java.io.*

val gson: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter())
    .create()

private fun getStorageDirectoryPath(): String {
    return File(Environment.getExternalStorageDirectory().absolutePath, "com.cicuro").path
}

fun getFile(filename: String): File {
    val file = File(getStorageDirectoryPath(), filename)
    if (!file.exists()) {
        try {
            val directory = File(getStorageDirectoryPath())
            directory.mkdirs()
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return file
}

fun <T> writeData(file: File, obj: T) {
    val json = gson.toJson(obj)
    val outputStream = FileOutputStream(file, false)
    val myOutWriter = OutputStreamWriter(outputStream)
    myOutWriter.append(json)
    myOutWriter.close()
    outputStream.close()
}

inline fun <reified T> readData(file: File): T? {
    val reader = JsonReader(FileReader(file))
    return gson.fromJson(reader, T::class.java)
}