package com.example.plutus.core.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.plutus.core.classes.*
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {

    @Insert
    fun insertTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM  transactions WHERE transactionId = :id")
    fun findTransactionById(id: Int): Transaction

    @Query("DELETE FROM transactions WHERE transactionId = :id")
    fun deleteTransactionById(id: Int)

    @Update(entity = Transaction::class,onConflict = OnConflictStrategy.REPLACE)
    fun update(transaction: Transaction)
    @Insert
    fun insert(join: PossedeCrossRef)

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): Flow<List<Possede>>



}