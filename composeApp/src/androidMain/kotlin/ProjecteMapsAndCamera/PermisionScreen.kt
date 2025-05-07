package ProjecteMapsAndCamera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Exercicis.BDD.database
import coil3.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    fun insert(title: String, y: Double, x : Double, url: String){
        mapsMarkerQueries.insert(title, y, x, url)
    }

    var surfaceRequest by mutableStateOf<SurfaceRequest?>(null)
        private set

    private val cameraPreview = Preview.Builder()
        .build()
        .also { preview ->
            preview.setSurfaceProvider { req ->
                surfaceRequest = req
            }
        }

    val imageCaptureUseCase = ImageCapture.Builder().build()

    // NUEVO: flujo con el último URI guardado
    private val _lastPhotoUri = MutableStateFlow<Uri?>(null)
    val lastPhotoUri: StateFlow<Uri?> = _lastPhotoUri

    fun onPhotoSaved(uri: android.net.Uri) {
        _lastPhotoUri.value = uri
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreview,
            imageCaptureUseCase
        )
        try { kotlinx.coroutines.awaitCancellation() }
        finally { cameraProvider.unbindAll() }
    }
}

/**
 * 3) takePhoto informado al VM
 */
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    viewModel: CameraViewModel2
) {
    val name = "photo_${System.nanoTime()}"
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOpts = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ).build()

    imageCapture.takePicture(
        outputOpts,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraX", "Error al guardar foto: ${exc.message}", exc)
            }
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let { uri ->
                    Log.d("CameraX", "Foto guardada en: $uri")
                    viewModel.onPhotoSaved(uri)  // informamos al VM
                }
            }
        }
    )
}

/**
 * 4) Pantalla de cámara que usa todo lo anterior
 */
@Composable
fun CameraScreen(
    navigateToAddMarkerScreen: () -> Unit
) {
    val viewModel: CameraViewModel2 = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1️⃣ Arranca la cámara
    LaunchedEffect(Unit) {
        viewModel.bindToCamera(context, lifecycleOwner)
    }

    // 2️⃣ Observa si se ha guardado una URI
    val savedUri by viewModel.lastPhotoUri.collectAsState()

    // 3️⃣ En cuanto savedUri != null, navega
    LaunchedEffect(savedUri) {
        if (savedUri != null) {
            navigateToAddMarkerScreen()
        }
    }

    Box(Modifier.fillMaxSize()) {
        // vista previa de la cámara
        viewModel.surfaceRequest?.let { req ->
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
                    takePhoto(
                        context,
                        viewModel.imageCaptureUseCase,
                        viewModel
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto")
            }
        }
    }
}

