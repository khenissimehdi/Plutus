package com.example.plutus.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plutus.core.classes.*
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.dao.ActionDao
import com.example.plutus.core.dao.BookletDao
import com.example.plutus.core.dao.CategoryDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import com.example.plutus.core.dao.TransactionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Transaction::class,
        Action::class,
        Booklet::class,
        Category::class,
        PossedeCrossRef::class],
    version = 10,
    exportSchema = true
)

abstract class PlutusRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): TransactionDao
    abstract fun bookletDao(): BookletDao
    abstract fun actionDao(): ActionDao
    abstract fun categoryDao(): CategoryDao

}



