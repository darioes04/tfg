package com.myprojects.myTickets.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.myprojects.prueba1.R

class GoogleAuthHelper(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)) // Usa el ID configurado en Firebase
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    companion object {
        fun getSignedInAccountFromIntent(data: Intent?): com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount> {
            return GoogleSignIn.getSignedInAccountFromIntent(data)
        }
    }
}

