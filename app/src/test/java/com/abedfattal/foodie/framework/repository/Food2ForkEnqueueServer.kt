package com.abedfattal.foodie.framework.repository

import com.abedfattal.foodie.framework.webservice.Food2ForkEndpoints
import com.abedfattal.foodie.helpers.EnqueueMockWebService

object Food2ForkEnqueueServer : EnqueueMockWebService {

    override val endpointsWithResponseFile: Map<String, String>
        get() = mapOf(
        Food2ForkEndpoints.recipeByCategory to "recipe-with-ingredient",
        Food2ForkEndpoints.recipeWithIngredient to "recipes-by-category",
    )
}