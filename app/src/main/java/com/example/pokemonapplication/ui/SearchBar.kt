package com.example.pokemonapplication.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelResId: Int = R.string.search
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        label = { Text(text = stringResource(id = labelResId)) }
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    PokemonApplicationTheme {
        SearchBar(value = "pikachu", onValueChange = {})
    }
}
