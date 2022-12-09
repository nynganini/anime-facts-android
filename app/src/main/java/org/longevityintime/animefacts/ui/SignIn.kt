package org.longevityintime.animefacts.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.longevityintime.animefacts.R
import org.longevityintime.animefacts.getActivity
import java.util.*
import kotlin.math.sign

@Composable
fun SignUp(
    onIdToken: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {  }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FacebookSignIn(onIdToken = onIdToken)
                    GoogleSignIn(onIdToken = onIdToken)
                }
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 32.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = stringResource(id = R.string.login_continue),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GoogleSignIn(
    modifier: Modifier = Modifier,
    onIdToken: (String) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val oneTapClient = Identity.getSignInClient(context)
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if(idToken != null){
                    onIdToken(idToken)
                }
            }
        }
    )

    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch {
                googleSignIn(context, launcher)
            }
        },
        modifier = modifier.padding(16.dp)) {
        Image(painter = painterResource(id = R.drawable.google_login_icon), contentDescription = null)
    }
}

private suspend fun googleSignIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context.getActivity()!!)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(R.string.google_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .build()
    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (_: Exception){}
}

private val LocalFacebookCallbackManager = staticCompositionLocalOf<CallbackManager> { error("No CallbackManager provided") }

@Composable
fun FacebookSignIn(
    modifier: Modifier = Modifier,
    onIdToken: (String) -> Unit
) {
    val callbackManager = CallbackManager.Factory.create()
    CompositionLocalProvider(LocalFacebookCallbackManager provides callbackManager) {
        FacebookLoginScreen(
            modifier,
            onIdToken = onIdToken
        )
    }
}
@Composable
fun FacebookLoginScreen(
    modifier: Modifier = Modifier,
    onIdToken: (String) -> Unit
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
                    onIdToken(result.accessToken.token)
                    Log.i("longevityintime", "onSuccess $result")
                }

            }
        )
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }
    val context = LocalContext.current
    IconButton(onClick = {
        LoginManager.getInstance().logInWithReadPermissions(context.getActivity()!!, Arrays.asList("public_profile"))
//        LoginManager.getInstance().logIn(context as ActivityResultRegistryOwner, callbackManager, listOf("email"))
    },
        modifier = modifier.padding(16.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.facebook_login_icon), contentDescription = null)
    }
}