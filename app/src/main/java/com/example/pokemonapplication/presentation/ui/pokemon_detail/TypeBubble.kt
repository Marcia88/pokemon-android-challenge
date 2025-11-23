package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonType
import com.example.pokemonapplication.domain.model.TypeModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

@Composable
fun TypeBubble(type: TypeModel) {
    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(12.dp),
                color = type.type?.backgroundColor() ?: colorResource(id = R.color.pokemon_type_normal)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (type.name.isNullOrEmpty().not()) {
            Text(
                text = type.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TypeBubblePreview() {
    PokemonApplicationTheme() {
        Column(Modifier.padding(10.dp)) {
            TypeBubble(type = TypeModel(name = "Fire"))
            Spacer(Modifier.height(10.dp))
            TypeBubble(type = TypeModel(name = "Fire", type = PokemonType.FIRE))
        }
    }
}