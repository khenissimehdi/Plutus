package com.example.plutus.core.dao

import androidx.room.*
import com.example.plutus.core.classes.CurrentBooklet

@Dao
interface CurrentBookletDao {
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrentBooklet(currentBooklet: CurrentBooklet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentBooklet(currentBooklet: CurrentBooklet)

    @Query("SELECT * FROM  currentBooklet LIMIT 1")
    fun getCurrentBooklet(): CurrentBooklet

    @Query("DELETE FROM currentBooklet WHERE currentBookletId = :id")
    fun deleteCurrentBooklet(id: Int)
}
