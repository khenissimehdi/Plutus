package com.example.plutus.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plutus.core.classes.Action
@Dao
interface ActionDao {

    @Insert
    fun insertCategory(action: Action)

    @Query("SELECT * FROM  `action` WHERE actionId = :id")
    fun findActionById(id: Int): Action

    @Query("DELETE FROM `action` WHERE actionId = :id")
    fun deleteTransactionById(id: Int)

    @Query("SELECT * FROM `action`")
    fun getAllTransaction(): List<Action>
}