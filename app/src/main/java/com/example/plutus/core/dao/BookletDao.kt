package com.example.plutus.core.dao

import androidx.room.*
import com.example.plutus.core.classes.BookLetAndTransactions
import com.example.plutus.core.classes.Booklet
import kotlinx.coroutines.flow.Flow

@Dao
interface BookletDao {
    @Insert
    fun insertBooklet(booklet: Booklet)

    @Query("SELECT * FROM  booklet WHERE bookletId = :id")
    fun findBookletById(id: Int): Booklet

    @Query("DELETE FROM booklet WHERE bookletId = :id")
    fun deleteBookletById(id: Int)

    @Transaction
    @Query("SELECT * FROM booklet")
    fun getBookletAndTransactions(): Flow<List<BookLetAndTransactions>>

    @Query("SELECT * FROM booklet")
    fun getAllBookLet(): Flow<List<Booklet>>


    @Update
    fun updateBooklet(booklet: Booklet)
}