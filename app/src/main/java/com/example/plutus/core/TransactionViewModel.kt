package com.example.plutus.core

import android.util.Log
import androidx.lifecycle.*
import com.example.plutus.Graph
import com.example.plutus.core.classes.*
import com.example.plutus.core.dao.BookletDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

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
        viewModelScope.launch {
             transactionRepo.allTransaction().collect {
                 _state.value = HomeViewState(transactions = it)
             }
        }
    }
}

data class HomeViewState(
    val transactions: List<Possede> = emptyList(),
)