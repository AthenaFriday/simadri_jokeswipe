// presentation/camera/ShareUtils.kt
package com.biggie.jokeswipe.presentation.camera

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    /**
     * Saves the given [bitmap] into the user's gallery (Pictures/JokeSwipe) under a
     * filename based on [displayName] and timestamp.
     */
    fun saveImageToGallery(context: Context, bitmap: Bitmap, displayName: String) {
        val filename = "$displayName-${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/JokeSwipe")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val uri: Uri? = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let { outUri ->
            context.contentResolver.openOutputStream(outUri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(outUri, contentValues, null, null)
            }
        }
    }

    /**
     * Shares the given [bitmap] along with the [joke] text by writing the bitmap to a
     * temporary cache file and launching a share chooser.
     */
    fun shareBitmap(context: Context, bitmap: Bitmap, joke: String) {
        // Write bitmap to cache
        val cacheFile = File(context.cacheDir, "shared_image.png")
        FileOutputStream(cacheFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Get a content URI with FileProvider
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cacheFile
        )

        // Create share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_TEXT, joke)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Launch chooser
        context.startActivity(
            Intent.createChooser(shareIntent, "Share your joke")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}