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
    version = 1,
    exportSchema = true
)

abstract class PlutusRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): TransactionDao

    private class PlutusDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.noteDao())
                }
            }
        }

        suspend fun populateDatabase(transactionDao: TransactionDao ) {
            var t = Transaction(2,"helo","000",200)
            transactionDao.insertTransaction(t)
        }
    }
    companion object {

        @Volatile
        private var INSTANCE: PlutusRoomDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context, scope: CoroutineScope): PlutusRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context, scope)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): PlutusRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PlutusRoomDatabase::class.java,
                "Plutus_database"
            ).addCallback(PlutusDatabaseCallback(scope))
                .build()
        }

    }
}



