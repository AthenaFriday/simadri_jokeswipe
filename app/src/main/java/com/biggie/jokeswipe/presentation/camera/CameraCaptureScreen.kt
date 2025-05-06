package com.biggie.jokeswipe.presentation.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.biggie.jokeswipe.R
import com.biggie.jokeswipe.presentation.navigation.Screen
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCaptureScreen(
    navController: NavController,
    jokeText: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val view = LocalView.current

    var previewUseCase by remember { mutableStateOf<Preview?>(null) }
    var imageCapture  by remember { mutableStateOf<ImageCapture?>(null) }
    var capturedUri   by remember { mutableStateOf<Uri?>(null) }

    // 1) Set up CameraX
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val rotation = view.display?.rotation ?: Surface.ROTATION_0
        val capture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .build()

        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                capture
            )
            previewUseCase = preview
            imageCapture  = capture
        } catch (e: Exception) {
            // TODO: show user‐facing error
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Capture") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Retake (discard last capture)
                IconButton(onClick = { capturedUri = null }) {
                    Icon(Icons.Default.Close, contentDescription = "Retake")
                }

                // Capture (or Confirm → Share)
                IconButton(onClick = {
                    if (capturedUri == null) {
                        // TAKE PHOTO
                        val photoFile = File(
                            context.cacheDir,
                            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                                .format(System.currentTimeMillis()) + ".jpg"
                        )
                        val opts = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                        imageCapture?.takePicture(
                            opts,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    Handler(Looper.getMainLooper()).post {
                                        capturedUri = rotateAndSave(photoFile)
                                    }
                                }
                                override fun onError(exception: ImageCaptureException) {
                                    // TODO: show error
                                }
                            }
                        )
                    } else {
                        // CONFIRM → NAVIGATE TO SHARE, passing both URI & the original joke
                        navController.navigate(
                            Screen.Share.shareWith(
                                uri       = capturedUri.toString(),
                                jokeText  = jokeText
                            )
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = "Capture",
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Quick‐Share button (once you’ve captured)
                IconButton(
                    onClick = {
                        capturedUri?.let { uri ->
                            navController.navigate(
                                Screen.Share.shareWith(
                                    uri       = uri.toString(),
                                    jokeText  = jokeText
                                )
                            )
                        }
                    },
                    enabled = (capturedUri != null)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (capturedUri == null) {
                // LIVE CAMERA PREVIEW
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    update = { pv ->
                        previewUseCase?.setSurfaceProvider((pv as PreviewView).surfaceProvider)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // SHOW FINAL CAPTURED IMAGE
                Image(
                    bitmap = BitmapFactory
                        .decodeStream(context.contentResolver.openInputStream(capturedUri!!))
                        .asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // BACK pressed while viewing = go back to live preview
    BackHandler(enabled = (capturedUri != null)) {
        capturedUri = null
    }
}

/**
 * Reads EXIF, rotates if needed, overwrites file, and returns its Uri
 */
private fun rotateAndSave(file: File): Uri {
    val exif = ExifInterface(file.absolutePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    val matrix = Matrix().apply {
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90  -> postRotate( 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
        }
    }
    val original = BitmapFactory.decodeFile(file.absolutePath)
    val rotated  = Bitmap.createBitmap(
        original, 0, 0, original.width, original.height, matrix, true
    )
    FileOutputStream(file).use { out ->
        rotated.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return Uri.fromFile(file)
}