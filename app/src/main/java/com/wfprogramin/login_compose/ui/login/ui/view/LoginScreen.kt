package com.wfprogramin.login_compose.ui.login.ui.view

import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.wfprogramin.login_compose.ui.theme.Shapes
import com.wfprogramin.login_compose.util.InputTypeInfo


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.wfprogramin.login_compose.R
import com.wfprogramin.login_compose.ui.login.ui.viewmodel.QuoteViewModel
import com.wfprogramin.login_compose.util.BasicValues
import kotlinx.coroutines.launch

private fun Context.doLogin() {
    Toast.makeText(
        this,
        "Something went wrong, try again later!",
        Toast.LENGTH_SHORT
    ).show()
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
    }

@Composable
fun Login(videoUri: Uri, quoteViewModel: QuoteViewModel = hiltViewModel()) {

    val email: String by quoteViewModel.email.observeAsState(initial = "")
    val password: String by quoteViewModel.password.observeAsState(initial = "")
    val isLoading: Boolean by quoteViewModel.isLoading.observeAsState(initial = false)

    val loginEnabled: Boolean by quoteViewModel.loginEnabled.observeAsState(initial = false)

    val context = LocalContext.current
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }

    val coroutineScope = rememberCoroutineScope()

    val value = AndroidView(
        factory = { it.buildPlayerView(exoPlayer) },
        modifier = Modifier.fillMaxSize()
    )
    DisposableEffect(
        value
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else {
        ProvideWindowInsets {
            Column(
                Modifier
                    .navigationBarsWithImePadding()
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    null,
                    Modifier.size(80.dp),
                    tint = Color.White
                )
                TextInput(
                    value = email,
                    InputTypeInfo.Name,
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
                    onTextFieldChange = {
                        quoteViewModel.onLoginChanged(
                            email = it,
                            password = password
                        )
                    },
                )
                TextInput(
                    value = password,
                    InputTypeInfo.Password,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        context.doLogin()
                    }),
                    focusRequester = passwordFocusRequester,
                    onTextFieldChange = {
                        quoteViewModel.onLoginChanged(
                            email = email,
                            password = it
                        )
                    },
                )
                LoginButton(
                    isEnabled = loginEnabled,
                    onClick =
                    {
                        coroutineScope.launch {
                            quoteViewModel.onLoginSelected()
                        }
                    }
                )
                Divider(
                    color = Color.White.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 48.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(BasicValues.notHaveAccout, color = Color.White)
                    TextButton(onClick = {}) {
                        Text(BasicValues.singUp)
                    }
                }
            }
        }
    }
}


@Composable
fun Body(){

}
@Composable
fun LoginButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(), enabled = isEnabled) {
        Text(BasicValues.signIn, Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun TextInput(
    value: String,
    inputType: InputTypeInfo,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions,
    onTextFieldChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, null) },
        label = { Text(text = inputType.label) },
        shape = Shapes.small,

        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )
}