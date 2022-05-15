package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "transactionId")
    var id: Int = 0;
    @ColumnInfo(name = "title")
    var title: String = "";
    @ColumnInfo(name = "date")
    var date: String = "";
    @ColumnInfo(name = "price")
    var price: Int = 0;

    constructor(){}

    constructor(id: Int, title: String, date: String, price: Int) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.price = price;
    }

    constructor(title: String, date: String, price: Int) {
        this.title = title;
        this.date = date;
        this.price = price;
    }
}