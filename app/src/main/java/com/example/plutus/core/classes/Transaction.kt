package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "transactions", /*foreignKeys = [
    ForeignKey(entity = Action::class, parentColumns = ["actionId"], childColumns = ["actionIdT"]),
    ForeignKey(entity = Booklet::class, parentColumns = ["bookletId"], childColumns = ["bookletIdT"])
]*/)
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
   // @ColumnInfo() @NonNull()
   // var actionIdT: Int = 0
  //  @ColumnInfo() @NonNull()
  //  var bookletIdT: Int = 0

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