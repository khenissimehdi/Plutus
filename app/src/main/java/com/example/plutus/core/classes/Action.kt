package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "action")
class Action {
    @PrimaryKey(autoGenerate = true)
    @NonNull()
    @ColumnInfo(name = "actionId")
    var id: Int = 0
    @ColumnInfo(name = "type")
    var type: String =  ""
    constructor(){}
    constructor(id: Int, type: String) {
        this.id = id
        this.type = type
    }
    constructor(type: String) {
        this.type = type
    }
}