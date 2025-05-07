package cat.itb.m78.exercices

import android.content.ContentValues
import android.content.Context
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.awaitCancellation
import kotlinx.serialization.Serializable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermission(navigateToCameraScreen:() -> Unit) {
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    if (cameraPermissionState.status.isGranted) {
        Text("Camera permission Granted")
        navigateToCameraScreen()
    } else {
        Column {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The camera is important for this app. Please grant the permission."

            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}
@Composable
fun CameraScreen(navigateToPhotosScreen:() -> Unit){
    val viewModel = viewModel{ CameraViewModel1() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }
    val surfaceRequest = viewModel.surferRequest.value
    val imageCaptureUseCase = viewModel.imageCaptureUseCase
    surfaceRequest?.let { request ->
        Box {
            CameraXViewfinder(
                surfaceRequest = request,
                modifier = Modifier.fillMaxSize()
            )
            Button({ takePhoto(context, imageCaptureUseCase) }){
                Text("Take Photo")
            }
            Button({navigateToPhotosScreen()}){
                Text("See the taken photos")
            }
        }
    }
}

@Composable
fun PhotosScreen(navigateToCameraScreen:() -> Unit){

}

private fun takePhoto(context: Context, imageCapture: ImageCapture) {
    val name = "photo_"+ System.nanoTime()
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
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraPreview", "Photo capture failed: ${exc.message}", exc)
            }
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.d("CameraPreview", "Photo capture succeeded: ${output.savedUri}")
            }
        }
    )
}

class CameraViewModel1() : ViewModel(){
    val surferRequest = mutableStateOf<SurfaceRequest?>(null)
    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            surferRequest.value = newSurfaceRequest
        }
    }
    val imageCaptureUseCase: ImageCapture = ImageCapture.Builder().build()
    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase, imageCaptureUseCase
        )
        try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
    }
}
object Destination {
    @Serializable
    data object FeatureThatRequiresCameraPermission
    @Serializable
    data object CameraScreen
    @Serializable
    data object PhotosScreen

}

@Composable
fun LibNavScreenSample() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.FeatureThatRequiresCameraPermission) {
        composable<Destination.FeatureThatRequiresCameraPermission>{
            FeatureThatRequiresCameraPermission(
                navigateToCameraScreen = { navController.navigate(Destination.CameraScreen) },
            )
        }
        composable<Destination.CameraScreen> {
            CameraScreen(
                navigateToPhotosScreen = { navController.navigate(Destination.PhotosScreen) }
            )
        }
        composable<Destination.PhotosScreen> {
            PhotosScreen (
                navigateToCameraScreen = { navController.navigate(Destination.CameraScreen) }
            )
        }
    }
}