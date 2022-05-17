package com.example.plutus.core

import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepo(private val transactionDao: TransactionDao) {
    fun allTransaction(): Flow<List<Possede>> = transactionDao.getAllTransaction();
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
}