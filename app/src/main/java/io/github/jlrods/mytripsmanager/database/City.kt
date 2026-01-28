package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cities",
    foreignKeys = [
        ForeignKey(
            entity = Country::class,
            parentColumns = ["id"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val countryId: Int
)