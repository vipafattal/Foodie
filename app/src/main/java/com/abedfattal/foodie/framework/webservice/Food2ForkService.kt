package com.abedfattal.foodie.framework.webservice

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Food2ForkService {

    @GET("get")
    suspend fun getRecipeWithIngredients(
        @Query("rId")
        recipeId: String
    ): Response<Food2ForkModels.RecipeDetails>

    @GET("search")
    suspend fun getRecipesByCategory(
        @Query("q")
        category: String
    ): Response<Food2ForkModels.Recipes>

}