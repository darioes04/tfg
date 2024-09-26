package com.myprojects.myTickets.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    private const val PERMISSION_REQUEST_CODE = 100

    // Verifica si los permisos ya han sido otorgados
    fun checkAndRequestPermissions(activity: ComponentActivity): Boolean {
        val permissionsToRequest = mutableListOf<String>()

        // Verifica si el permiso de la cámara ha sido concedido
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        // Verifica si el permiso de almacenamiento ha sido concedido
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        return if (permissionsToRequest.isNotEmpty()) {
            // Solicita los permisos si no han sido concedidos
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
            false
        } else {
            true // Todos los permisos ya han sido otorgados
        }
    }

    // Maneja la respuesta de la solicitud de permisos
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onSuccess() // Todos los permisos fueron concedidos
            } else {
                onFailure() // Algún permiso fue denegado
            }
        }
    }
}
