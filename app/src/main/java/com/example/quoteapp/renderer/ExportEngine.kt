package com.example.quoteapp.renderer

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.quoteapp.model.EditorState
import com.example.quoteapp.model.ExportFormat
import com.example.quoteapp.model.ExportQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object ExportEngine {

    suspend fun exportImage(
        context: Context,
        state: EditorState,
        format: ExportFormat = state.exportSettings.format,
        quality: ExportQuality = state.exportSettings.quality
    ): Uri? = withContext(Dispatchers.Default) {
        try {
            val width = if (state.exportSettings.useCustomSize && state.exportSettings.customWidth > 0)
                state.exportSettings.customWidth else state.canvasSize.width
            val height = if (state.exportSettings.useCustomSize && state.exportSettings.customHeight > 0)
                state.exportSettings.customHeight else state.canvasSize.height

            val bitmap = QuoteRenderer.renderToBitmap(state, width, height, context)

            val mimeType = format.mimeType
            val extension = format.extension
            val fileName = "quote_${System.currentTimeMillis()}.$extension"

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToMediaStore(context, bitmap, fileName, mimeType, format, quality)
            } else {
                saveToExternalStorage(context, bitmap, fileName, format, quality)
            }

            bitmap.recycle()
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createShareIntent(context: Context, uri: Uri, mimeType: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun createSaveIntent(uri: Uri, mimeType: String, fileName: String): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun saveToMediaStore(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        mimeType: String,
        format: ExportFormat,
        quality: ExportQuality
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QuoteApp")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                when (format) {
                    ExportFormat.PNG -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    ExportFormat.JPEG -> bitmap.compress(Bitmap.CompressFormat.JPEG, quality.value, outputStream)
                    ExportFormat.WEBP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, quality.value, outputStream)
                    } else {
                        @Suppress("DEPRECATION")
                        bitmap.compress(Bitmap.CompressFormat.WEBP, quality.value, outputStream)
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)
            }
        }
        return uri
    }

    private fun saveToExternalStorage(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        format: ExportFormat,
        quality: ExportQuality
    ): Uri? {
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QuoteApp")
        dir.mkdirs()
        val file = File(dir, fileName)

        FileOutputStream(file).use { outputStream ->
            when (format) {
                ExportFormat.PNG -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                ExportFormat.JPEG -> bitmap.compress(Bitmap.CompressFormat.JPEG, quality.value, outputStream)
                ExportFormat.WEBP -> bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, quality.value, outputStream)
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
