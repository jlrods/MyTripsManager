package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [
        Index(value = ["name", "countryId"], unique = true)
    ]
)
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val countryId: Int
)