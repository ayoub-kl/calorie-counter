package com.calorieai.app.data.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Compresses an image from a content URI for upload: reduces resolution and
 * encodes as JPEG to keep memory and upload size safe while preserving
 * recognition quality. Call from a background thread (e.g. Dispatchers.IO).
 *
 * @return Compressed temp file suitable for upload, or null if compression fails.
 *         Caller must delete the returned file when done.
 */
fun compressImageForUpload(context: Context, uri: Uri): File? {
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream, null, opts)
    } ?: return null
    val w = opts.outWidth
    val h = opts.outHeight
    if (w <= 0 || h <= 0) return null

    val sampleSize = computeSampleSize(w, h, MAX_LONG_EDGE)
    val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream, null, decodeOpts)
    } ?: return null

    return try {
        val outFile = File(context.cacheDir, "upload_compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outFile).use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
        }
        bitmap.recycle()
        outFile
    } catch (_: Exception) {
        bitmap.recycle()
        null
    }
}

private const val MAX_LONG_EDGE = 1280
private const val JPEG_QUALITY = 85

private fun computeSampleSize(width: Int, height: Int, maxLongEdge: Int): Int {
    var size = 1
    val longEdge = maxOf(width, height)
    while (longEdge / size > maxLongEdge) {
        size *= 2
    }
    return size.coerceAtLeast(1)
}
