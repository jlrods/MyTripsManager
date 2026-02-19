package io.github.jlrods.mytripsmanager.ui.screens.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.ProviderRepository
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

    fun insertProvider(name: String, logoRes: Int?, logoUri: String?) {
        viewModelScope.launch {
            repository.insert(
                Provider(
                    name = name,
                    logoRes = logoRes,
                    logoUri = logoUri
                )
            )
        }
    }
}