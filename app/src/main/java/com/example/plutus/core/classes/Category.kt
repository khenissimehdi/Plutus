package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.*


@Entity(tableName = "category")
class Category {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "categoryId")
    var id: Long = 0
    @ColumnInfo(name = "title")
    var title: String = ""


    constructor(){}
    constructor(id: Long, title: String) {
        this.id = id
        this.title = title
    }
    constructor(title: String) {
        this.title = title
    }
}