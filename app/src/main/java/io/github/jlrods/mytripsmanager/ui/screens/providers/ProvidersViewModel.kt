package io.github.jlrods.mytripsmanager.ui.screens.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.ProviderRepository
import io.github.jlrods.mytripsmanager.database.City
import io.github.jlrods.mytripsmanager.database.Provider
import kotlinx.coroutines.launch

class ProvidersViewModel(
    private val repository: ProviderRepository
) : ViewModel() {

    val providers = repository.providers

    fun deleteProvider(provider: Provider) {
        viewModelScope.launch {
            repository.delete(provider)
        }
    }

    fun insertProvider(
        name: String,
        logoRes: Int?,
        logoUri: String?,
        onDuplicate: () -> Unit,
        onSuccess: () -> Unit
    ){
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
    fun updateProvider(id: Int, name: String, logRes: Int?, logoUri: String?) {
        viewModelScope.launch {
            repository.update(
                Provider(
                    id = id,
                    name = name.trim().lowercase(),
                    logoRes = logRes,
                    logoUri = logoUri
                )
            )
        }
    }
}