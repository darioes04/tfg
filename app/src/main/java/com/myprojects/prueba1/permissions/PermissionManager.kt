package com.myprojects.prueba1.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    const val PERMISSION_REQUEST_CODE = 100

    // Verifica si los permisos están otorgados
    fun checkAndRequestPermissions(activity: ComponentActivity) {
        val cameraPermission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val permissionsToRequest = mutableListOf<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }



        // Método para verificar si el usuario denegó permanentemente los permisos
        fun shouldShowPermissionRationale(activity: ComponentActivity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
        }
    }
}
