package com.example.plutus.core

import androidx.annotation.WorkerThread
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.Category
import com.example.plutus.core.dao.CategoryDao
import kotlinx.coroutines.flow.Flow

data class CategoryRepo(val categoryDao: CategoryDao) {
    fun allCategories(): Flow<List<Category>> = categoryDao.getAllCategories();
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(category: Category): Long {
        return categoryDao.insertCategory(category = category);
    }
}