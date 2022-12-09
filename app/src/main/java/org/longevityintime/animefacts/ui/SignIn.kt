package org.longevityintime.animefacts.ui

import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun SignUp(

) {
    Scaffold(
        topBar = {

        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            FacebookSignIn()
        }
    }
}

private val LocalFacebookCallbackManager = staticCompositionLocalOf<CallbackManager> { error("No CallbackManager provided") }

@Composable
fun FacebookSignIn(

) {

    val callbackManager = CallbackManager.Factory.create()
    CompositionLocalProvider(LocalFacebookCallbackManager provides callbackManager) {
        FacebookLoginScreen()
    }
}
@Composable
fun FacebookLoginScreen(

) {
    val callbackManager = LocalFacebookCallbackManager.current
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.i("longevityintime", "onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.i("longevityintime", "onError $error")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.i("longevityintime", "onSuccess $result")
                }

            }
        )
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }
    val context = LocalContext.current
    Button(onClick = {
//        LoginManager.getInstance().logInWithReadPermissions(context.getActivity()!!, Arrays.asList("public_profile"))
        LoginManager.getInstance().logIn(context as ActivityResultRegistryOwner, callbackManager, listOf("email"))
    }) {
        Text(text = "FB Login")
    }
}