package com.myprojects.myTickets.utils

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

object GalleryUtils {

    fun selectImageFromGallery(activity: ComponentActivity, galleryLauncher: ActivityResultLauncher<Intent>, onImageSelected: (Uri?) -> Unit) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }
}
