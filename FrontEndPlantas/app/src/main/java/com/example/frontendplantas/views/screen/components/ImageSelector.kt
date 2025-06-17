package com.example.frontendplantas.views.screen.components

import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.utils.FileManager
import com.example.frontendplantas.utils.ImageCompressor
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun ImageSelectorPreview(){}

@Composable
fun ImageSelector(
    imageCompressor: ImageCompressor,
    fileManager: FileManager,
    modifier: Modifier = Modifier
){
    /*var compressedImage by remember { // lo dejo comentado x si quiero ahcer algo con la imagen
        mutableStateOf<ByteArray?>(null)
    }*/
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {contentUri ->
        if(contentUri == null){
            return@rememberLauncherForActivityResult
        }
        val mimeType = context.contentResolver.getType(contentUri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        scope.launch {
            fileManager.saveImage(
                contentUri = contentUri,
                fileName = "uncompressed.$extension"
            )
        }

        scope.launch {
            val compressedImage=imageCompressor.compressImage(
                contentUri = contentUri,
                compressionThreshold = 200 * 1024L

            )

            fileManager.saveAndVerifyImage(
                bytes = compressedImage ?: return@launch,
                fileName = "compressed1.$extension"
            )

            fileManager.saveImage(
                bytes = compressedImage ?: return@launch,
                fileName = "compressed.$extension"
            )
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            modifier = Modifier.align(Alignment.Center) // <-- esto va aquí
        ) {
            Text("Subir foto")
        }
    }

}