package com.example.plutus
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.plutus.core.ActionRepo
import com.example.plutus.core.BookletRepo
import com.example.plutus.core.CategoryRepo
import com.example.plutus.core.TransactionRepo
import com.example.plutus.core.classes.*
import com.example.plutus.core.dao.CategoryDao
import com.example.plutus.db.PlutusRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    val bookletRepository by lazy {
        BookletRepo(
            bookletDao = database.bookletDao()
        )
    }

    val actionRepository by lazy {
        ActionRepo(
            actionDao = database.actionDao()
        )
    }

    val categryRepository by lazy {
        CategoryRepo(
            categoryDao = database.categoryDao()
        )
    }
    fun provide(context: Context) {
        database = Room.databaseBuilder(context, PlutusRoomDatabase::class.java, "plutus2.db").build()
       // database.clearAllTables()
    }


    // TEST FUNC
    fun pop() {

       var booklet =  database.bookletDao()
        var t = database.noteDao()
        var c = database.categoryDao()
        var categ = Category(1,"Meca")
        var relation =  PossedeCrossRef(1,1)
        var ac = database.actionDao()
        CoroutineScope(Dispatchers.IO).launch {
         //  database.clearAllTables()
            /* t.getAllTransaction().collect {
               Log.i("hel", it.size.toString())
           }*/

            c.insertCategory(categ)
           t.insert(PossedeCrossRef(2,1))
            t.insertTransaction(Transaction("Things","today",2000,2,0))

          /*  ac.getTransactionsBindToAction().collect {
                if(!it.isEmpty()) {
                   it.forEach { e ->
                       run {
                           if (!e.transactions.isEmpty()) {
                               e.transactions.forEach {
                                   Log.i("helo", it.title)
                               }
                           }
                       }
                   }
                }
            }*/
        }
    }
}