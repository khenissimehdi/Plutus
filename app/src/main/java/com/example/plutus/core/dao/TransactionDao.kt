package com.example.plutus.core.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plutus.core.classes.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {

    @Insert
    fun insertTransaction(transactionDao: Transaction)

    @Query("SELECT * FROM  transactions WHERE transactionId = :id")
    fun findTransactionById(id: Int): Transaction

    @Query("DELETE FROM transactions WHERE transactionId = :id")
    fun deleteTransactionById(id: Int)


    @Insert
    fun insert(join: PossedeCrossRef)

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): Flow<List<Possede>>



}