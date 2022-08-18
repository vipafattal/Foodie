package com.abedfattal.foodie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abedfattal.foodie.models.Recipe
import com.abedfattal.foodie.ui.theme.FoodieTheme
import com.skydoves.landscapist.glide.GlideImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    buildUI()
                }
            }
        }
    }

    @Composable
    private fun buildUI() {
        val viewModel: MainActivityViewModel = viewModel()

        val searchResultsState by viewModel.searchResults.observeAsState(emptyList())
        val searchState by viewModel.searchQuery.observeAsState("")

        LazyColumn {
            items(searchResultsState) {
                BuildRecipeItem(recipe = it)
            }
        }
    }
}

@Composable
fun BuildRecipeItem(recipe: Recipe) {

    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Card(
            Modifier
                .width(IntrinsicSize.Max)
                .shadow(elevation = 0.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.2.dp, androidx.compose.ui.graphics.Color.LightGray),
        ) {
            Column(Modifier.fillMaxWidth()) {
                GlideImage(
                    modifier = Modifier.height(180.dp),
                    imageModel = recipe.image,
                    previewPlaceholder = R.drawable.poster
                )
                Surface(
                    Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = recipe.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(Modifier.padding(top = 6.dp))
                        Text(
                            text = recipe.publisher,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoodieTheme {
        LazyColumn() {
            items(10) {
                BuildRecipeItem(
                    recipe = Recipe(
                        id = "",
                        title = "Joolss favourite beef stew",
                        publisher = "Jamie Oliver",
                        source = "",
                        image = "",
                        ingredients = null
                    ),
                )
            }
        }
    }
}
