package org.longevityintime.animefacts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.ui.AnimeListScreen
import org.longevityintime.animefacts.ui.AnimeNavHost
import org.longevityintime.animefacts.ui.theme.AnimeFactsTheme
import org.longevityintime.animefacts.viewmodel.AnimeFactVieModel
import org.longevityintime.animefacts.viewmodel.AnimeFactVieModel.Companion.ANIME_KEY
import org.longevityintime.animefacts.viewmodel.AnimeViewModel

class MainActivity : ComponentActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        if(auth.currentUser == null){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AnimeFactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimeNavHost(navController = rememberNavController())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    companion object {
        private const val ANONYMOUS = "anonymous"
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