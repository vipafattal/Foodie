package com.abedfattal.foodie.ui.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abedfattal.foodie.FoodieApplication
import kotlinx.coroutines.flow.map

object UserPreferences {

    private val context: Context = FoodieApplication.app
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


    private val CATEGORY_KEY = stringPreferencesKey("selected_category")

    val selectedCategory = context.dataStore.data.map {
        val defaultCategory = "pizza"
        it[CATEGORY_KEY] ?: defaultCategory
    }

    suspend fun setSelectCategory(newCategory: String){
        context.dataStore.edit {
            it[CATEGORY_KEY] = newCategory
        }
    }
}

