package com.example.plutus.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(private val repo: TransactionRepo): ViewModel() {
    val allTransaction: LiveData<List<Transaction>> = repo.allTransaction.asLiveData()

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repo.insert(transaction = transaction)
    }
}