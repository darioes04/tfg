package com.myprojects.myTickets.permissions

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings


object PermissionManager {
    private const val PERMISSION_REQUEST_CODE = 100

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

    // Maneja el resultado de la solicitud de permisos
    fun handlePermissionsResult(
        activity: ComponentActivity,
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Si alguno de los permisos fue denegado
            if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                // Mostrar mensaje de cómo proceder
                AlertDialog.Builder(activity)
                    .setTitle("Permisos Requeridos")
                    .setMessage(
                        "Los permisos de cámara y almacenamiento son necesarios para continuar. " +
                                "Por favor, habilítelos manualmente en Configuración > Aplicaciones > MyTickets > Permisos."
                    )
                    .setPositiveButton("Abrir Configuración") { _, _ ->
                        // Abre la configuración de la aplicación
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", activity.packageName, null)
                        }
                        activity.startActivity(intent)
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}
