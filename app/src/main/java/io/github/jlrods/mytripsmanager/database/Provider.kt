package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class Provider(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val logo: String
)