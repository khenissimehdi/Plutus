package com.example.plutus.core.classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "Possede", primaryKeys = ["transactionId", "categoryId"])
class Possede {
    var transactionId: Int = 0
    var categoryId: Int = 0
}