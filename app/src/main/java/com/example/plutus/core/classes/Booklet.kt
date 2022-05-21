package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booklet")
class Booklet {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookletId")
    var bookletId: Int = 0;
    @ColumnInfo(name = "title")
    var title: String = "";
    @ColumnInfo(name = "date")
    var date: String = "";

    constructor(){}

    constructor(id: Int, title: String, date: String) {
        this.bookletId = id;
        this.title = title;
        this.date = date;
    }

    constructor(title: String, date: String) {
        this.title = title;
        this.date = date;
    }
}