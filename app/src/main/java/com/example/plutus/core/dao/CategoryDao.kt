package com.example.plutus.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plutus.core.classes.Category

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: Category)

    @Query("SELECT * FROM  category WHERE categoryId = :id")
    fun findCategById(id: Int): Category

    @Query("DELETE FROM category WHERE categoryId = :id")
    fun deleteTransactionById(id: Int)

    @Query("SELECT * FROM category")
    fun getAllTransaction(): List<Category>
}