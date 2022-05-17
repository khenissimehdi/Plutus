package com.example.plutus.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plutus.core.classes.*
import com.example.plutus.core.classes.Transaction
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
    version = 3,
    exportSchema = true
)

abstract class PlutusRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): TransactionDao
}



