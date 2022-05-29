package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currentBooklet")
class CurrentBooklet {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "currentBookletId")
    var id: Long = 0

    constructor(){}
    constructor(id: Long) {
        this.id = id
    }
}