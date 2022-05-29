package com.example.plutus.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plutus.core.classes.*
import com.example.plutus.core.classes.Transaction
import com.example.plutus.core.classes.typeConverter.Converters
import com.example.plutus.core.dao.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Transaction::class,
        Action::class,
        Booklet::class,
        Category::class,
        PossedeCrossRef::class,
        CurrentBooklet::class],
    version = 5,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class PlutusRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): TransactionDao
    abstract fun bookletDao(): BookletDao
    abstract fun currentBookletDao(): CurrentBookletDao
    abstract fun actionDao(): ActionDao
    abstract fun categoryDao(): CategoryDao

}



