package com.example.plutus.core

import androidx.lifecycle.*
import com.example.plutus.Graph
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch


@OptIn(InternalCoroutinesApi::class)
class TransactionViewModel(private val transactionRepo: TransactionRepo = Graph.categoryRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    private val _selectedCategory = MutableStateFlow(Transaction())

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onCategorySelected(category: Transaction) {
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
            Transaction(2,"helo","hello",100)

        )
        viewModelScope.launch(Dispatchers.IO) {
            list.forEach { category -> transactionRepo.insert(category) }
        }
    }
}

data class HomeViewState(
    val categories: List<Transaction> = emptyList(),
    val selectedCategory: Transaction? = null
)