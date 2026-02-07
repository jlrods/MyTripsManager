package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY name ASC")
    fun getAllProviders(): Flow<List<Provider>>

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProvider(id: Int): Flow<Provider>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(provider: Provider)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(providers: List<Provider>)

    @Update
    suspend fun update(provider: Provider)

    @Delete
    suspend fun delete(provider: Provider)
}