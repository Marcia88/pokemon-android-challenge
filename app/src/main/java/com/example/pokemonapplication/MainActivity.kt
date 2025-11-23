package com.example.pokemonapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokemonapplication.presentation.ui.pokemon_list.PokemonListScreen
import com.example.pokemonapplication.presentation.PokemonListViewModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.Text

// Navigation imports
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapplication.presentation.ui.pokemon_detail.PokemonDetail
import com.example.pokemonapplication.domain.model.PokemonDetailModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: PokemonListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonApplicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("list") {
                            PokemonListScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = vm,
                                onItemClick = { detail: PokemonDetailModel ->
                                    val name = detail.name
                                    navController.navigate("detail/${name}")
                                }
                            )
                        }

                        composable(
                            route = "detail/{name}",
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name")
                            val detail = vm.getPokemonDetail(name.orEmpty())
                            PokemonDetail(pokemonDetail = detail)
                        }
                    }
                }
            }
        }
    }
}