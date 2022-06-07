package com.example.plutus.core

import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Action
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.PossedeCrossRef
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.dao.PossedeCrossRefDao
import com.example.plutus.core.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepo(private val transactionDao: TransactionDao, private val possedeCrossRefDao: PossedeCrossRefDao) {
    fun allTransaction(): Flow<List<Possede>> = transactionDao.getAllTransaction();
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTransactionGategAndAction(transaction: Transaction, categoryId: Int) {
        val id = transactionDao.insertTransaction(transaction = transaction)
        transactionDao.insert(join = PossedeCrossRef(transactionId = id.toInt(), categoryId = categoryId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTransactionById(id: Int): Transaction {
        return transactionDao.findTransactionById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateById(transaction: Transaction, categoryId: Int) {
        var possedeRef = possedeCrossRefDao.findTransactionById(transaction.id)
        possedeRef.categoryId = categoryId
        possedeCrossRefDao.update(transaction.id, categoryId)
        transactionDao.update(transaction = transaction)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: Int) {
        transactionDao.deleteTransactionById(id = id)
    }
}