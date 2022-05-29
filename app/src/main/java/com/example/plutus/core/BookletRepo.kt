package com.example.plutus.core

import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.dao.BookletDao
import com.example.plutus.core.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

class BookletRepo(private val bookletDao: BookletDao) {
    fun allBooklets(): Flow<List<Booklet>> = bookletDao.getAllBookLet();
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(booklet: Booklet) {
        bookletDao.insertBooklet(booklet = booklet);
    }
}