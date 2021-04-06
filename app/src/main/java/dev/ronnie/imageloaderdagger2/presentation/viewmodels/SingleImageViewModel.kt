package dev.ronnie.imageloaderdagger2.presentation.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ronnie.imageloaderdagger2.data.repository.Repository
import dev.ronnie.imageloaderdagger2.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/

@Suppress("deprecation", "BlockingMethodInNonBlockingContext")
class SingleImageViewModel @Inject constructor(
    private val app: Application,
    private val repo: Repository
) :
    AndroidViewModel(app) {
    var imageString: String? = null
    val shouldRound = false

    private val _notifyDownloading = MutableLiveData<Int>()
    val notifyDownloading: LiveData<Int> get() = _notifyDownloading

    fun saveImage(
        bitmap: Bitmap, photoId: String
    ) = viewModelScope.launch(Dispatchers.Default) {
        try {
            val fos: OutputStream?
            var imageFile: File? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = app.applicationContext.contentResolver
                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "Image${System.currentTimeMillis()}"
                )
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + File.separator.toString() + "UnsplashImages"
                )
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            } else {
                val imagesDir: String = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM
                ).toString() + File.separator.toString() + "UnsplashImages"
                imageFile = File(imagesDir)
                if (!imageFile.exists()) {
                    imageFile.mkdir()
                }
                imageFile = File(imagesDir, "Image${System.currentTimeMillis()}.png")
                fos = FileOutputStream(imageFile)
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos?.flush()
            fos?.close()
            if (imageFile != null) {
                MediaScannerConnection.scanFile(
                    app.applicationContext,
                    arrayOf(imageFile.toString()),
                    null,
                    null
                )
            }
            withContext(Dispatchers.Main) {
                _notifyDownloading.value = HAS_SAVED
                app.applicationContext.toast("Image saved successfully")
                try {
                    repo.sendDownload(photoId)
                } catch (_: Exception) {

                }
            }
        } catch (_: Exception) {
            withContext(Dispatchers.Main) {
                _notifyDownloading.value = ERROR_DOWNLOADING
                app.applicationContext.toast("There was a problem saving the image")
            }
        }

    }

    suspend fun getBitmapFromURL(src: String, photoId: String) {
        withContext(Dispatchers.Main) {
            _notifyDownloading.value = STARTING_DOWNLOAD
        }
        try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            saveImage(bitmap, photoId).also {
                withContext(Dispatchers.Main) {
                    _notifyDownloading.value = HAS_DOWNLOADED
                }

            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                _notifyDownloading.value = ERROR_DOWNLOADING
                app.applicationContext.toast("There was a problem downloading the image")
            }

        }
    }

}