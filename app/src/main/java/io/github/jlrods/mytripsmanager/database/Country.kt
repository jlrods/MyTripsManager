package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "countries",
    indices = [
        Index(value = ["name"], unique = true)
    ]
    )
data class Country(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val currency: String,
    val flagRes: Int
)