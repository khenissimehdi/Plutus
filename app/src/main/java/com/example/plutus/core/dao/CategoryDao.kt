package com.example.plutus.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.plutus.core.classes.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: Category): Long

    @Query("SELECT * FROM  category WHERE categoryId = :id")
    fun findCategoryById(id: Int): Category

    @Query("DELETE FROM category WHERE categoryId = :id")
    fun deleteCategoryById(id: Int)


    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<Category>>
}