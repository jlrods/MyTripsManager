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


    suspend fun insert(provider: Provider) =
        providerDao.insert(provider)

    suspend fun update(provider: Provider) =
        providerDao.update(provider)

    suspend fun delete(provider: Provider) =
        providerDao.delete(provider)
}
