package com.example.plutus.core

import androidx.lifecycle.*
import com.example.plutus.Graph
import com.example.plutus.core.classes.*
import com.example.plutus.core.dao.BookletDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch


@OptIn(InternalCoroutinesApi::class)
class ActionViewModel(private val actionRepo: ActionRepo = Graph.actionRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    private val _selectedCategory = MutableStateFlow(Action())

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onCategorySelected(category: Action) {
        _selectedCategory.value = category
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
                print("helo databae")

        }

        loadCategoriesFromDb()
    }

    private fun loadCategoriesFromDb() {
        val list = mutableListOf(
            Action(2,"Shrimp")
        )

        viewModelScope.launch(Dispatchers.IO) {
            list.forEach { category -> actionRepo.insert(category) }
        }
    }
}