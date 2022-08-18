package com.abedfattal.foodie.framework.webservice

import com.abedfattal.foodie.models.Recipe

object Food2ForkModels {

    data class Recipes(val recipes: List<Recipe>)
    data class RecipeDetails(val recipe: Recipe)

}