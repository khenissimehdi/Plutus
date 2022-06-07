package com.example.plutus.core

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.CurrentBooklet
import com.example.plutus.core.dao.CategoryDao
import com.example.plutus.core.dao.CurrentBookletDao
import kotlinx.coroutines.flow.Flow

class CurrentBookletRepo(val currentBookletDao: CurrentBookletDao) {
    fun currentBookLet(): CurrentBooklet? = currentBookletDao.getCurrentBooklet()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(currentBooklet: CurrentBooklet, oldBookletId: Int) {
        currentBookletDao.updateCurrentBooklet(currentBooklet = currentBooklet)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(currentBooklet: CurrentBooklet, oldBookletId: Int) {

        currentBookletDao.deleteCurrentBooklet(oldBookletId)
        currentBookletDao.insertCurrentBooklet(currentBooklet = currentBooklet)
    }




}