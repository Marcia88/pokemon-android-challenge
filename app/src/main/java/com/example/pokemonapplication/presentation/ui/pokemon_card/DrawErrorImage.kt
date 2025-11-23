package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

@Composable
fun DrawErrorImage() {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Image load error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(R.string.image_load_failed),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview
@Composable
fun DrawErrorImagePreview() {
    PokemonApplicationTheme {
        DrawErrorImage()
    }
}