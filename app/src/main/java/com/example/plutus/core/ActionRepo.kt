package com.example.plutus.core

import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Action
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.dao.ActionDao
import com.example.plutus.core.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

class ActionRepo(private val actionDao: ActionDao) {
    fun allTransaction(): Flow<List<Action>> = actionDao.getAllActions();
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(action: Action) {
        actionDao.insertCategory(action = action)
    }


}