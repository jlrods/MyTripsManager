package io.github.jlrods.mytripsmanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

object ImageStorage {

    fun saveCompressedImage(
        context: Context,
        uri: Uri,
        maxSize: Int = 256
    ): String {

        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val resizedBitmap = resizeBitmap(originalBitmap, maxSize)

        val directory = File(context.filesDir, "provider_logos")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(
            directory,
            "provider_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(file).use { output ->
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
        }

        return file.absolutePath
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {

        val ratio = minOf(
            maxSize.toFloat() / bitmap.width,
            maxSize.toFloat() / bitmap.height
        )

        val width = (bitmap.width * ratio).roundToInt()
        val height = (bitmap.height * ratio).roundToInt()

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}