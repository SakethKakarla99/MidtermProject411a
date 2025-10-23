package com.example.midtermproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RecipeApp() }
    }
}


@Composable
fun RecipeApp() {
    val vm: RecipeViewModel = viewModel()
    val nav = rememberNavController()

    MaterialTheme {
        NavHost(navController = nav, startDestination = "home") {


            composable("home") {
                HomeScreen(
                    viewModel = vm,
                    onOpenDetails = { id -> nav.navigate("details/$id") },
                    onOpenFavorites = { nav.navigate("favorites") }
                )
            }


            composable(
                route = "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                DetailsScreen(
                    viewModel = vm,
                    recipeId = id,
                    onBack = { nav.popBackStack() }
                )
            }

            // Favorites -> only saved recipes
            composable("favorites") {
                FavoritesScreen(
                    viewModel = vm,
                    onOpenDetails = { id -> nav.navigate("details/$id") },
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}


class RecipeViewModel : ViewModel() {

    val recipes = listOf(
        Recipe(
            1, "Paneer Butter Masala", "Creamy tomato curry",
            listOf("Paneer", "Tomatoes", "Cream", "Butter"),
            listOf("Sauté aromatics", "Add tomato + spices", "Simmer with cream", "Add paneer")
        ),
        Recipe(
            2, "Avocado Toast", "Simple breakfast",
            listOf("Bread", "Avocado", "Lemon", "Salt", "Pepper"),
            listOf("Toast bread", "Mash avocado + season", "Spread & serve")
        ),
        Recipe(
            3, "Chicken Stir-Fry", "Quick weeknight meal",
            listOf("Chicken", "Bell peppers", "Soy sauce", "Garlic", "Ginger"),
            listOf("Slice chicken", "Stir-fry veggies", "Cook chicken", "Combine & serve")
        )

    )

    // State for favorites
    private val favoriteIds = mutableStateListOf<Int>()
    fun isFavorite(id: Int) = id in favoriteIds
    fun toggleFavorite(id: Int) {
        if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
    }
    fun favoriteRecipes(): List<Recipe> = recipes.filter { it.id in favoriteIds }
}

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>
)


@Composable
fun HomeScreen(
    viewModel: RecipeViewModel,
    onOpenDetails: (Int) -> Unit,
    onOpenFavorites: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        HeaderRow(title = "Recipes", rightButton = "Favorites", onRight = onOpenFavorites)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.recipes) { r ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onOpenDetails(r.id) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(r.title, fontWeight = FontWeight.Bold)
                        Text(r.description)
                    }
                    Button(onClick = { viewModel.toggleFavorite(r.id) }) {
                        Text(if (viewModel.isFavorite(r.id)) "★" else "☆")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    viewModel: RecipeViewModel,
    recipeId: Int,
    onBack: () -> Unit
) {
    val r = viewModel.recipes.firstOrNull { it.id == recipeId } ?: return
    val isFav = viewModel.isFavorite(recipeId)

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        HeaderRow(title = r.title, leftButton = "Back", onLeft = onBack)
        Spacer(Modifier.height(8.dp))

        Text(r.description)
        Spacer(Modifier.height(8.dp))

        Text("Ingredients:", fontWeight = FontWeight.Bold)
        r.ingredients.forEach { Text("• $it") }

        Spacer(Modifier.height(8.dp))
        Text("Steps:", fontWeight = FontWeight.Bold)
        r.steps.forEachIndexed { i, s -> Text("${i + 1}. $s") }

        Spacer(Modifier.height(12.dp))
        Button(onClick = { viewModel.toggleFavorite(r.id) }) {
            Text(if (isFav) "Remove Favorite" else "Add Favorite")
        }
    }
}

@Composable
fun FavoritesScreen(
    viewModel: RecipeViewModel,
    onOpenDetails: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favs = viewModel.favoriteRecipes()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        HeaderRow(title = "Favorites", leftButton = "Back", onLeft = onBack)
        Spacer(Modifier.height(8.dp))

        if (favs.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No favorites yet. ☆ Add some from Home.")
            }
        } else {
            LazyColumn {
                items(favs) { r ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetails(r.id) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(r.title, fontWeight = FontWeight.Bold)
                            Text(r.description)
                        }
                        Button(onClick = { viewModel.toggleFavorite(r.id) }) { Text("★") }
                    }
                }
            }
        }
    }
}

// Simple header (no experimental app bar APIs)
@Composable
fun HeaderRow(
    title: String,
    leftButton: String? = null,
    rightButton: String? = null,
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (leftButton != null && onLeft != null) {
            Button(onClick = onLeft, modifier = Modifier.padding(end = 8.dp)) { Text(leftButton) }
        }
        Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (rightButton != null && onRight != null) {
            Button(onClick = onRight, modifier = Modifier.padding(start = 8.dp)) { Text(rightButton) }
        }
    }
}


