package com.example.pokemonapplication.presentation.ui.search

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelResId: Int = R.string.search,
    onSearch: ((String) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusRequester.requestFocus()
                })
            }
            .onFocusChanged { state ->
                isFocused = state.isFocused
            }
            .onKeyEvent { keyEvent ->
                val native = keyEvent.nativeKeyEvent
                if (native.keyCode == android.view.KeyEvent.KEYCODE_ENTER
                    && native.action == android.view.KeyEvent.ACTION_UP
                ) {
                    onSearch?.invoke(value)
                    focusManager.clearFocus()
                    true
                } else {
                    false
                }
            },
        label = { Text(text = stringResource(id = labelResId)) },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch?.invoke(value)
            focusManager.clearFocus()
        })
    )

    LaunchedEffect(isFocused) {
        if (isFocused) {
            delay(80)
            keyboardController?.show()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    PokemonApplicationTheme {
        SearchBar(value = "pikachu", onValueChange = {}, onSearch = {})
    }
}