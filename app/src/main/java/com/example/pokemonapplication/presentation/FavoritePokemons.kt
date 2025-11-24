package com.example.pokemonapplication.presentation

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pokemonapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemons(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as? Activity
    TopAppBar(
        title = { Text(text = stringResource(R.string.favorites)) },
        navigationIcon = {
            IconButton(onClick = {
                if (!navController.navigateUp()) {
                    activity?.finish()
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FavoritePokemonsPreview() {
    val navController = rememberNavController()
    FavoritePokemons(navController)
}