package com.example.plutus.core.classes

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class Possede( @Embedded val transaction: Transaction,
                    @Relation(
                        parentColumn = "transactionId",
                        entityColumn = "categoryId",
                        associateBy = Junction(PossedeCrossRef::class)
                    )
                    var categories: List<Category> = arrayListOf()
)
