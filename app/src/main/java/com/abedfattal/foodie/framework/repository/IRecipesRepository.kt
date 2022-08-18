package com.abedfattal.foodie.framework.repository

import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.models.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipesRepository {

    fun getRecipeWithIngredient(recipeId:String): Flow<ProcessState<Recipe>>
    fun getRecipesByCategory(category: String): Flow<ProcessState<List<Recipe>>>

}