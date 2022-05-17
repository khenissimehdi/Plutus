package com.example.plutus
import android.content.Context
import androidx.room.Room
import com.example.plutus.core.TransactionRepo
import com.example.plutus.db.PlutusRoomDatabase

/**
 * A simple singleton dependency graph
 *
 * For a real app, please use something like Koin/Dagger/Hilt instead
 */
object Graph {
    lateinit var database: PlutusRoomDatabase

    val categoryRepository by lazy {
        TransactionRepo(
            transactionDao = database.noteDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, PlutusRoomDatabase::class.java, "plutus.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .build()
    }
}