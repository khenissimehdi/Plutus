package com.example.plutus.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.plutus.core.classes.Action
import com.example.plutus.core.classes.ActionAndTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {

    @Insert
    fun insertCategory(action: Action)

    @Query("SELECT * FROM  `action` WHERE actionId = :id")
    fun findActionById(id: Int): Action

    @Query("DELETE FROM `action` WHERE actionId = :id")
    fun deleteTransactionById(id: Int)
    @Transaction
    @Query("SELECT * FROM `action`")
    fun getTransactionsBindToAction() : Flow<List<ActionAndTransactions>>

    @Query("SELECT * FROM `action`")
    fun getAllActions(): Flow<List<Action>>
}