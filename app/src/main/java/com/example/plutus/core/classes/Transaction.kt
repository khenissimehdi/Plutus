package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "transactions")
class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "transactionId")
    var id: Int = 0
    @ColumnInfo(name = "title")
    var title: String = ""
    @ColumnInfo(name = "date")
    var date: Date = Date()
    @ColumnInfo(name = "price")
    var price: Int = 0;
    @ColumnInfo(name = "actionIdT")
    var actionIdT: Int = 0
    @ColumnInfo(name = "bookletIdT" )
    var bookletIdT: Int = 0

    constructor(){}

    constructor(id: Int, title: String, date: Date, price: Int, actionIdT: Int, bookletIdT: Int) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.price = price;
        this.actionIdT = actionIdT
        this.bookletIdT  = bookletIdT
    }

    constructor(title: String, date: Date, price: Int, actionIdT: Int, bookletIdT: Int) {
        this.title = title;
        this.date = date;
        this.price = price;
        this.bookletIdT = bookletIdT
        this.actionIdT = actionIdT
    }
}