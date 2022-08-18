package com.abedfattal.foodie.models

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("recipe_id")
    val id: String,
    val title: String,
    val publisher: String,
    @SerializedName("source_url")
    val source: String,
    @SerializedName("image_url")
    val image: String,
    val ingredients: List<String>?,
)