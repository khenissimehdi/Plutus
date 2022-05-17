package com.example.plutus.core.classes

import androidx.room.Entity

@Entity(primaryKeys = ["transactionId", "categoryId"])
class PossedeCrossRef {
    var transactionId: Int = 0
    var categoryId: Int = 0
}