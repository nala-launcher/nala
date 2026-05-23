package data.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

import java.io.File
import java.io.FileOutputStream

class IconCacheManager(context: Context) {
    private val iconDir = File(context.filesDir, "system_icons").apply {
        if (!exists()) mkdirs()
    }

    fun saveIconAndGetPath(packageName: String, drawable: Drawable): String {
        val file = File(iconDir, "$packageName.webp")
        val bitmap = drawableToBitmap(drawable)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out)
        }
        return file.absolutePath
    }

    fun deleteIcon(packageName: String) {
        val file = File(iconDir, "$packageName.webp")
        if (file.exists()) file.delete()
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 144
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 144
        return drawable.toBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
}
