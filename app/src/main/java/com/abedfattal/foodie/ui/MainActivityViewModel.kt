package com.abedfattal.foodie.ui

import androidx.lifecycle.*
import com.abedfattal.foodie.framework.repository.RecipesRepository
import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.models.Recipe

class MainActivityViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {


    private val _selectedCategory: MutableLiveData<String> = MutableLiveData("pizza")
    val selectedCategory: LiveData<String> get() = _selectedCategory

    val searchResults: LiveData<List<Recipe>> =
        Transformations.switchMap(selectedCategory, ::runQuranSearch)

    private val _searchProcessState: MutableLiveData<ProcessState<Unit>> = MutableLiveData()
    val searchProcessState: LiveData<ProcessState<Unit>> get() = _searchProcessState

    fun submitNewCategory(newQuery: String) {
        if (newQuery != _selectedCategory.value)
            _selectedCategory.postValue(newQuery)
    }


    private fun runQuranSearch(query: String): LiveData<List<Recipe>> {
        return liveData {
            if (query.isEmpty())
                ProcessState.Success<List<Recipe>>(emptyList())
            else {
                recipesRepository.getRecipesByCategory(category = query).collect { processState ->
                    _searchProcessState.postValue(processState.transformProcessType())
                    if (processState is ProcessState.Success)
                        emit(processState.data!!)
                }

            }
        }
    }
}