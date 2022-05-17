package com.example.plutus.core.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.plutus.core.classes.Booklet


interface BookletDao {
    @Insert
    fun insertCategory(booklet: Booklet)

    @Query("SELECT * FROM  booklet WHERE bookletId = :id")
    fun findBookletById(id: Int): Booklet

    @Query("DELETE FROM booklet WHERE bookletId = :id")
    fun deleteTransactionById(id: Int)

    @Query("SELECT * FROM booklet")
    fun getAllTransaction(): List<Booklet>
}