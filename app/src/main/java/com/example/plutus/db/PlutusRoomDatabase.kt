package com.example.plutus.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import com.example.plutus.core.dao.TransactionDao

@Database(
    entities = [TransactionDao::class],
    version = 1,
    exportSchema = true
)
abstract class PlutusRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): TransactionDao
    companion object {

        @Volatile
        private var INSTANCE: PlutusRoomDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context): PlutusRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): PlutusRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PlutusRoomDatabase::class.java,
                "Plutus_database"
            )
                .build()
        }
    }
}

