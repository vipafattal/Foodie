package com.abedfattal.foodie.ui

import androidx.lifecycle.*
import com.abedfattal.foodie.framework.repository.IRecipesRepository
import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.models.Recipe

class MainActivityViewModel(private val recipesRepository: IRecipesRepository) : ViewModel() {


    private val _searchQuery: MutableLiveData<String> = MutableLiveData("pizza")
    val searchQuery: LiveData<String> get() = _searchQuery

    val searchResults: LiveData<List<Recipe>> =
        Transformations.switchMap(searchQuery, ::runQuranSearch)

    private val _searchProcessState: MutableLiveData<ProcessState<Unit>> = MutableLiveData()
    private val searchProcessState: LiveData<ProcessState<Unit>> get() = _searchProcessState

    fun submitNewSearch(newQuery: String) {
        _searchQuery.postValue(newQuery)
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