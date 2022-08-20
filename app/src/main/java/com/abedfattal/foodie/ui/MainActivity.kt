package com.abedfattal.foodie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.abedfattal.foodie.R.drawable
import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.models.Recipe
import com.abedfattal.foodie.ui.preferences.UserPreferences
import com.abedfattal.foodie.ui.theme.FoodieTheme
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {

    private val categories = listOf("pizza", "beef", "turkey", "chocolate", "donuts", "no_cat_test")
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

        val viewModel: MainActivityViewModel = getViewModel()
        val recipesList by viewModel.searchResults.observeAsState(emptyList())
        val searchState by viewModel.searchProcessState.observeAsState(ProcessState.Success())
        val selectedCategory by UserPreferences.selectedCategory.onEach {
            viewModel.submitNewCategory(
                it
            )
        }.collectAsState("")
        Column {
            buildCategoriesList(selectedCategory)
            when (val process = searchState) {
                is ProcessState.Loading -> buildLoadingProgressBar()
                is ProcessState.Failed -> buildErrorView(selectedCategory, process)
                else -> buildRecipeList(recipesList)
            }
        }
    }

    @Composable
    private fun buildErrorView(
        selectedCategory: String,
        process: ProcessState.Failed<Unit>
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Unable to load data for $selectedCategory, reason:${
                    stringResource(
                        process.friendlyMsg
                    )
                }"
            )
        }
    }

    @Composable
    private fun buildLoadingProgressBar() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


    @Composable
    private fun buildCategoriesList(
        selectedCategory: String,
    ) {
        Surface(
            modifier = Modifier
                .heightIn(60.dp, 80.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(size = 0.dp), shadowElevation = 4.dp
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                items(categories) {
                    CategoryItem(it, selectedCategory == it)
                }
            }
        }
    }

    @Composable
    private fun buildRecipeList(searchResultsState: List<Recipe>) {
            LazyColumn {
                items(searchResultsState) {
                    BuildRecipeItem(recipe = it)
                }
        }

    }

    @Composable
    fun CategoryItem(
        category: String,
        isSelected: Boolean
    ) {
        Button(
            modifier = Modifier.padding(end = 8.dp),
            onClick = {
                lifecycleScope.launch {
                    UserPreferences.setSelectCategory(category)
                }
            },
            shape = RoundedCornerShape(size = 8.dp),
            colors = if (isSelected) ButtonDefaults.buttonColors()
            else
                ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(category.capitalize())
        }
    }

    @Composable
    fun BuildRecipeItem(recipe: Recipe) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 0.dp)
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 0.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.LightGray),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    CoilImage(
                        imageModel = recipe.image,
                        contentScale = ContentScale.Crop,
                        previewPlaceholder = drawable.poster,
                        modifier = Modifier.height(180.dp),
                    )
                    Surface(
                        Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Column(
                            Modifier
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
            Column {
                buildCategoriesList("Pizza")
                buildRecipeList((1..4).map {
                    Recipe(
                        id = "",
                        title = "Joolss favourite beef stew",
                        publisher = "Jamie Oliver",
                        source = "",
                        image = "",
                        ingredients = null
                    )
                })
            }
        }
    }
}
