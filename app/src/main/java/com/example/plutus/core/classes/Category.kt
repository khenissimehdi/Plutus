package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.*


@Entity(tableName = "category")
class Category {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "categoryId")
    var id: Int = 0
    @ColumnInfo(name = "title")
    var title: String = ""
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "transactionId",
        associateBy = Junction(Possede::class)
    )
    var transactions: List<Transaction> = arrayListOf();

    constructor(){}
    constructor(id: Int, title: String, transactions: List<Transaction>) {
        this.id = id
        this.title = title
        this.transactions = transactions
    }
    constructor(title: String, transactions: List<Transaction>) {
        this.title = title
        this.transactions = transactions
    }
}