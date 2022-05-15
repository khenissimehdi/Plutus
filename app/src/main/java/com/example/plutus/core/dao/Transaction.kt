package com.example.plutus.core.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.example.plutus.core.classes.Transaction

interface Transaction {

    @Insert
    fun insertTransaction(transactionDao: Transaction)

    @Query("SELECT * FROM  transactions WHERE transactionId = :id")
    fun findTransactionById(id: Int): Transaction

    @Query("DELETE FROM transactions WHERE transactionId = :id")
    fun deleteTransactionById(id: Int)

    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): LiveData<List<Transaction>>
}