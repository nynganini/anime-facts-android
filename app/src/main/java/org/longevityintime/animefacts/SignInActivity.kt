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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.longevityintime.animefacts.ui.SignUp
import org.longevityintime.animefacts.ui.theme.AnimeFactsTheme

class SignInActivity: ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AnimeFactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.onBackground
                ) {
                    SignUp()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null){
            val signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .build()
            signIn.launch(signInIntent)
        }
        else {
            gotoMainActivity()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if(result.resultCode == RESULT_OK){
            gotoMainActivity()
        }
        else {
            Toast.makeText(this, "There was an error signing in", Toast.LENGTH_LONG).show()
        }
    }
    private fun gotoMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}