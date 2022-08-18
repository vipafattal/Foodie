package com.abedfattal.foodie.framework.repository

import com.abedfattal.foodie.framework.utils.newRequest
import com.abedfattal.foodie.framework.utils.transform
import com.abedfattal.foodie.framework.webservice.Food2ForkService
import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.models.Recipe
import kotlinx.coroutines.flow.Flow

class RecipesRepository(private val food2ForkService: Food2ForkService) : IRecipesRepository {
    override fun getRecipeWithIngredient(recipeId: String): Flow<ProcessState<Recipe>> {
        return newRequest { food2ForkService.getRecipeWithIngredients(recipeId) }
            .transform { it.recipe }
    }

    override fun getRecipesByCategory(category: String): Flow<ProcessState<List<Recipe>>> {
        return newRequest { food2ForkService.getRecipesByCategory(category) }
            .transform { it.recipes }
    }

}