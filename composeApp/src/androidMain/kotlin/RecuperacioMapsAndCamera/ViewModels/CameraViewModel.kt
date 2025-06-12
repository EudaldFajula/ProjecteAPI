package RecuperacioMapsAndCamera.ViewModels

import RecuperacioMapsAndCamera.PHOTO_URI_KEY
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.awaitCancellation
import com.russhwolf.settings.Settings

class CameraViewModel : ViewModel(){
    val settings : Settings = Settings()

    val surferRequest = mutableStateOf<SurfaceRequest?>(null)
    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            surferRequest.value = newSurfaceRequest
        }
    }

    private val imageCaptureUseCase: ImageCapture = ImageCapture.Builder().build()
    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase, imageCaptureUseCase
        )
        try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
    }

    private val _savedPhotoUri = mutableStateOf<Uri?>(null)
    val savedPhotoUri: State<Uri?> = _savedPhotoUri

    fun takePhoto(context: Context) {
        val name = "photo_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Markers")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(resolver, collection, contentValues)
            .build()

        imageCaptureUseCase.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraViewModel",
                        "Error al tomar foto: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        output.savedUri?.let { resolver.update(it, contentValues, null, null) }
                    }
                    Log.d("CameraViewModel", "Foto guardada: ${output.savedUri}")
                    _savedPhotoUri.value = output.savedUri

                    //Settings examen
                    output.savedUri?.let { settings.putString(PHOTO_URI_KEY, it.toString()) }
                }
            }
        )
    }
}