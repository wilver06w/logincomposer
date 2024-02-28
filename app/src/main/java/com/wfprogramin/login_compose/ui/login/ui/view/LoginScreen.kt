package com.wfprogramin.login_compose.ui.login.ui.view


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.wfprogramin.login_compose.R
import com.wfprogramin.login_compose.ui.login.ui.viewmodel.LoginViewModel
import com.wfprogramin.login_compose.ui.theme.Shapes
import com.wfprogramin.login_compose.util.BasicValues
import com.wfprogramin.login_compose.util.InputTypeInfo
import kotlinx.coroutines.launch

@Composable
fun Login(quoteViewModel: LoginViewModel = hiltViewModel()) {

    val email: String by quoteViewModel.email.observeAsState(initial = "")
    val password: String by quoteViewModel.password.observeAsState(initial = "")
    val isLoading: Boolean by quoteViewModel.isLoading.observeAsState(initial = false)

    val loginEnabled: Boolean by quoteViewModel.loginEnabled.observeAsState(initial = false)

    val context = LocalContext.current
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        quoteViewModel
            .toastMessage
            .collect { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }


    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        ProvideWindowInsets {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.nubes),
                    "Background",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.matchParentSize(),
                )
            }
            Column(
                Modifier
                    .navigationBarsWithImePadding()
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    BasicValues.welcome, color = Color.White, fontSize = 28.sp,
                    modifier = Modifier
                        .offset(
                            x = 2.dp,
                            y = 2.dp
                        )
                        .alpha(0.75f)
                )
                Spacer(
                    modifier = Modifier.height(
                        10 .dp
                    )
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
                        quoteViewModel.sendMessage("Algo salió mal, ¡inténtalo de nuevo más tarde!")
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
                        quoteViewModel.sendMessage("Algo salió mal, ¡inténtalo de nuevo más tarde!")
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
                    Text(BasicValues.notHave_Account, color = Color.White)
                    TextButton(onClick = {}) {
                        Text(BasicValues.singUp)
                    }
                }
            }
        }
    }
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