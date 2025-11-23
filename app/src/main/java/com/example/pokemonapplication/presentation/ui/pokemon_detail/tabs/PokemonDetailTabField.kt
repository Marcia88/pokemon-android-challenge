package com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PokemonDetailTabField(field: String, fieldValue: String) {
    Row(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = field,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = fieldValue,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PokemonDetailTabField(field: String, fieldValues: List<String>) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = field,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            fieldValues.forEachIndexed { index, value ->
                Text(
                    text = "• $value",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (index < fieldValues.size - 1) Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
