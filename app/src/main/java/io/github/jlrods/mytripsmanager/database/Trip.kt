package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val start: Long,
    val end: Long,
    val cashBudget: Double,
    val cashSpent: Double,
    val totalCost: Double
)