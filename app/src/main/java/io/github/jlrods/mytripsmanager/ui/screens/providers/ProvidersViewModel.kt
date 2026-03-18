package io.github.jlrods.mytripsmanager.ui.screens.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.ProviderRepository
import io.github.jlrods.mytripsmanager.database.Provider
import kotlinx.coroutines.launch
import java.io.File

class ProvidersViewModel(
    private val repository: ProviderRepository
) : ViewModel() {

    val providers = repository.providers

    fun insertProvider(
        name: String,
        logoRes: Int?,
        logoUri: String?,
        onDuplicate: () -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val cleanName = name.trim().lowercase()

            if (repository.existsByName(cleanName)) {
                onDuplicate()
                return@launch
            }
            repository.insert(
                Provider(
                    name = cleanName,
                    logoRes = logoRes,
                    logoUri = logoUri
                )
            )

            onSuccess()
        }
    }

    fun updateProvider(
        id: Int,
        name: String,
        logRes: Int?,
        logoUri: String?,
        onDuplicate: () -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val cleanName = name.trim().lowercase()
            val duplicate = repository.existsDuplicateForUpdate(cleanName, id)

            if (duplicate) {
                onDuplicate()
                return@launch
            } else {
                repository.update(
                    Provider(
                        id = id,
                        name = cleanName,
                        logoRes = logRes,
                        logoUri = logoUri
                    )
                )

                onSuccess()
            }


        }
    }

    fun deleteProvider(provider: Provider) {
        viewModelScope.launch {
            repository.delete(provider)
        }
        provider.logoUri?.let { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

}