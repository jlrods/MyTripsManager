package io.github.jlrods.mytripsmanager.ui.screens.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jlrods.mytripsmanager.data.ProviderRepository

class ProvidersViewModelFactory(
    private val repository: ProviderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProvidersViewModel(repository) as T
    }
}