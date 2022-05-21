package com.example.plutus.core.classes

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class BookLetAndTransactions(@Embedded val booklet: Booklet,
                                 @Relation(
                                     parentColumn = "bookletId",
                                     entityColumn = "bookletIdT"
                                 ) val transactions: List<Transaction>
)


data class ActionAndTransactions(@Embedded val action: Action,
                                 @Relation(
                                     parentColumn = "actionId",
                                     entityColumn = "actionIdT"

                                 ) val transactions: List<Transaction>
)


@Entity(primaryKeys = ["transactionId", "categoryId"])
data class PossedeCrossRef (
    var transactionId: Int,
    var categoryId: Int
)
