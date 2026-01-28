package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "destinations",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Destination(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val cityId: Int,
    val start: Long,
    val end: Long
)