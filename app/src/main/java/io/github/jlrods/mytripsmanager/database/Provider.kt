package io.github.jlrods.mytripsmanager.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "providers",
    indices = [Index(value = ["name"], unique = true)]
)
data class Provider(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val logoRes: Int? = null,        // for resource image
    val logoUri: String? = null      // for gallery image
)