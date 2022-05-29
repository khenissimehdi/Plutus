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
    suspend fun insert(title: String, date: String, price: Int, actionId: Int, bookletId: Int, categoryId: Int) {
            val transaction = Transaction(title, date, price, actionId, bookletId)
            transactionRepo.insertTransactionGategAndAction(transaction = transaction, categoryId = categoryId)
    }

    suspend fun updateById(transaction: Transaction) {
        transactionRepo.updateById(transaction = transaction);
    }

    suspend fun getTransactionById(id: Int): Transaction {
        return transactionRepo.getTransactionById(id = id)
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