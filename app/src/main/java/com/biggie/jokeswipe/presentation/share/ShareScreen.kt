package com.biggie.jokeswipe.presentation.share

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.biggie.jokeswipe.R
import com.biggie.jokeswipe.presentation.camera.ShareUtils
import com.biggie.jokeswipe.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    navController: NavController,
    imageUriString: String,
    jokeText: String
) {
    val context = LocalContext.current

    // decode the bitmap once
    val bitmapState = produceState<Bitmap?>(null, imageUriString) {
        runCatching {
            val uri = Uri.parse(imageUriString)
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
        }.onSuccess { value = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share Joke") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Joke.route) {
                            popUpTo(Screen.Joke.route) { inclusive = false }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
            )
        },
        bottomBar = {
            // only download
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    bitmapState.value?.let { bmp ->
                        ShareUtils.saveImageToGallery(context, bmp, "joke_image")
                        Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(painterResource(R.drawable.download), contentDescription = "Download")
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            bitmapState.value?.let { bmp ->
                // full‐screen image
                Image(
                    bitmap             = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier           = Modifier.fillMaxSize()
                )
                // **overlay** joke text on top of image
                Text(
                    text        = jokeText,
                    color       = Color.White,
                    textAlign   = TextAlign.Left,
                    modifier    = Modifier
                        .align(Alignment.Center)
                        .background(Color(0x80000000))
                        .padding(16.dp)
                )
            } ?: run {
                // loading spinner
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShareScreenPreview() {
    ShareScreen(
        navController     = NavController(LocalContext.current),
        imageUriString    = "content://com.android.providers.media.documents/document/image%3A1234",
        jokeText          = "This is a joke"
    )
}
