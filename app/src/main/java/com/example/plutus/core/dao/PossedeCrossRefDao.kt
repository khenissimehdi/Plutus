package com.example.plutus.core.dao

import androidx.room.*
import com.example.plutus.core.classes.PossedeCrossRef


@Dao
interface PossedeCrossRefDao {


    @Query("SELECT * FROM PossedeCrossRef WHERE transactionId = :idT")
    fun findTransactionById(idT: Int): PossedeCrossRef

    @Query("UPDATE PossedeCrossRef SET categoryId=:idC WHERE transactionId = :idT")
    fun update(idT: Int,idC: Int)
}


