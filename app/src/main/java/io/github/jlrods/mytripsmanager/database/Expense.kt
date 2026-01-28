package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExpenseType::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Provider::class,
            parentColumns = ["id"],
            childColumns = ["providerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val tripId: Int,
    val typeId: Int,
    val providerId: Int,
    val date: Long,
    val cost: Double
)