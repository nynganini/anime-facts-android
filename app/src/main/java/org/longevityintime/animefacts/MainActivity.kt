package org.longevityintime.animefacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import org.longevityintime.animefacts.ui.theme.AnimeFactsTheme

class MainActivity : ComponentActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

//        Firebase.initialize(this)
//        auth = Firebase.auth

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AnimeFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignIn()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null) reload()
    }
}

@Composable
fun SignIn() {
    Text(text = "Hello world!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnimeFactsTheme {
        SignIn()
    }
}