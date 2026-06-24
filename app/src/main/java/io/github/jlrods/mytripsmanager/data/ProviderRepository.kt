package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Provider
import io.github.jlrods.mytripsmanager.database.ProviderDao
import kotlinx.coroutines.flow.Flow

class ProviderRepository(
    private val providerDao: ProviderDao
) {

    val providers: Flow<List<Provider>> =
        providerDao.getAllProviders()

    suspend fun existsByName(name: String): Boolean {
        return providerDao.countByName(name) > 0
    }

    suspend fun existsDuplicateForUpdate(name: String, id: Int): Boolean {
        return providerDao.existsDuplicateForUpdate(name,id) > 0
    }

    fun getAllProviders(): Flow<List<Provider>> {
        return providerDao.getAllProviders()
    }


    suspend fun insert(provider: Provider) =
        providerDao.insert(provider)

    suspend fun update(provider: Provider) =
        providerDao.update(provider)

    suspend fun delete(provider: Provider) =
        providerDao.delete(provider)
}
