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
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.camera.core.CameraSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCaptureScreen(
    navController: NavController,
    // receive the joke text under the name "jokeText"
    jokeText: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val view = LocalView.current

    var previewUseCase by remember { mutableStateOf<Preview?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var capturedUri by remember { mutableStateOf<Uri?>(null) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val rotation = view.display?.rotation ?: Surface.ROTATION_0
        val capture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                capture
            )
            previewUseCase = preview
            imageCapture = capture
        } catch (e: Exception) {
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Capture") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Retake
                IconButton(onClick = { capturedUri = null }) {
                    Icon(Icons.Default.Close, contentDescription = "Retake")
                }

                // Capture / Confirm
                IconButton(onClick = {
                    if (capturedUri == null) {
                        // take picture
                        val photoFile = File(
                            context.cacheDir,
                            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                                .format(Date()) + ".jpg"
                        )
                        val outputOpts = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                        imageCapture?.takePicture(
                            outputOpts,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    Handler(Looper.getMainLooper()).post {
                                        capturedUri = rotateAndSave(photoFile)
                                    }
                                }
                                override fun onError(exc: ImageCaptureException) {
                                    // handle error
                                }
                            }
                        )
                    } else {
                        // Already captured → navigate to Share, now using "jokeText"
                        navController.navigate(
                            "${Screen.Share.route}" +
                                    "?uri=${Uri.encode(capturedUri.toString())}" +
                                    "&jokeText=${Uri.encode(jokeText)}"
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = "Capture",
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Share (same deep‐link, enabled only after capture)
                IconButton(
                    onClick = {
                        capturedUri?.let { uri ->
                            navController.navigate(
                                "${Screen.Share.route}" +
                                        "?uri=${Uri.encode(uri.toString())}" +
                                        "&jokeText=${Uri.encode(jokeText)}"
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
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    update = { previewView ->
                        previewUseCase?.setSurfaceProvider((previewView as PreviewView).surfaceProvider)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
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

    BackHandler(enabled = (capturedUri != null)) {
        capturedUri = null
    }
}

private fun rotateAndSave(file: File): Uri {
    val exif = ExifInterface(file.absolutePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    val matrix = Matrix().apply {
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90  -> postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
            else                                -> return@apply
        }
    }
    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
    val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    FileOutputStream(file).use { out ->
        rotated.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return Uri.fromFile(file)
}