package com.example.plutus.core

import androidx.lifecycle.*
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(private val repo: TransactionRepo): ViewModel() {
    val allTransaction: LiveData<List<Possede>> = repo.allTransaction.asLiveData()

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repo.insert(transaction = transaction)
    }
}

class WordViewModelFactory(private val repository: TransactionRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}