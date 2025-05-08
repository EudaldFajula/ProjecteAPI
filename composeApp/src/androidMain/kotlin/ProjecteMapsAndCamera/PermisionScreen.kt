package ProjecteMapsAndCamera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Exercicis.BDD.database
import com.google.accompanist.permissions.*
import kotlinx.coroutines.awaitCancellation

/**
 * 1) Pedimos permisos de cámara y, si < Android 10, de almacenamiento
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(navigateToCameraScreen: () -> Unit) {
    val cameraPerm = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePerm = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    // Si estamos en < Q, necesitamos WRITE; si no, basta cámara:
    val allGranted =
        cameraPerm.status.isGranted &&
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                        storagePerm.status.isGranted)

    // Cuando TODOS estén concedidos, navegamos:
    LaunchedEffect(allGranted) {
        if (allGranted) navigateToCameraScreen()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (allGranted) {
            Text("Permisos concedidos, abriendo cámara…")
        } else {
            Text(
                if (cameraPerm.status.shouldShowRationale ||
                    (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && storagePerm.status.shouldShowRationale)
                ) {
                    "Necesitamos cámara (y almacenamiento en versiones antiguas) para guardar la foto."
                } else {
                    "Por favor concede los permisos para continuar."
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                cameraPerm.launchPermissionRequest()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    storagePerm.launchPermissionRequest()
                }
            }) {
                Text("Conceder permisos")
            }
        }
    }
}

/**
 * 2) ViewModel que además guarda el último URI
 */
class CameraViewModel2 : ViewModel() {
    //Data base
    val mapsMarkerQueries = database.mapsQueries
    var all = mapsMarkerQueries.selectAll().executeAsList()
    fun insert(title: String, y: Double, x: Double, url: String) {
        mapsMarkerQueries.insert(title, y, x, url)
    }

    val surferRequest = mutableStateOf<SurfaceRequest?>(null)
    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            surferRequest.value = newSurfaceRequest
        }
    }
    val imageCaptureUseCase: ImageCapture = ImageCapture.Builder().build()
    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(
            lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase, imageCaptureUseCase
        )
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }

    // NUEVO: flujo con el último URI guardado
    val lastPhotoUri = mutableStateOf<Uri?>(null)

    fun onPhotoSaved(uri: Uri) {
        lastPhotoUri.value = uri
    }


    fun takePhoto(context: Context, navigateToAddMarkerScreen: () -> Unit) {
        val resolver = context.contentResolver
        val name = "photo_" + System.nanoTime()
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCaptureUseCase.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(
                        "CameraViewModel",
                        "Error al tomar foto: ${exc.message}", exc
                    )
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        output.savedUri?.let { resolver.update(it, contentValues, null, null) }
                    }
                    Log.d("CameraViewModel", "Foto guardada: ${output.savedUri}")
                    lastPhotoUri.value = output.savedUri
                    navigateToAddMarkerScreen()

                }

            })
    }
}

/**
 * 4) Pantalla de cámara que usa todo lo anterior
 */
@Composable
fun CameraScreen(
    navigateToAddMarkerScreen: () -> Unit
) {
    val viewModel = viewModel{CameraViewModel2() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }
    val surfaceRequest = viewModel.surferRequest.value

    Box(Modifier.fillMaxSize()) {
        // vista previa de la cámara
        surfaceRequest?.let { req ->
            CameraXViewfinder(
                surfaceRequest = req,
                modifier = Modifier.fillMaxSize()
            )
        }

        // botón de disparo abajo
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.takePhoto(
                        context,
                        navigateToAddMarkerScreen
                    )

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto")
            }
        }
    }
}





