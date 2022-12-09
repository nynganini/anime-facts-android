package org.longevityintime.animefacts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.longevityintime.animefacts.ui.SignUp
import org.longevityintime.animefacts.ui.theme.AnimeFactsTheme

class SignInActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AnimeFactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.onBackground
                ) {
                    SignUp(
                        onIdToken = { idToken ->
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener{
                                    gotoMainActivity()
                                }
                        },
                        onAccessToken = {
                            val firebaseCredential = FacebookAuthProvider.getCredential(it)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener{
                                    gotoMainActivity()
                                }
                        }
                    )
                }
            }
        }

    }

    private fun gotoMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}