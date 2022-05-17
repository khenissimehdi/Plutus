package com.example.plutus.db

import android.app.Application
import com.example.plutus.core.TransactionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob

class PlutusApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    @OptIn(InternalCoroutinesApi::class)
    val database by lazy { PlutusRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TransactionRepo(database.noteDao())}
}

