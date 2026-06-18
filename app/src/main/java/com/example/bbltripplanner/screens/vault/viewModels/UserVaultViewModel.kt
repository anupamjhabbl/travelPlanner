package com.example.bbltripplanner.screens.vault.viewModels

import androidx.lifecycle.ViewModel
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.screens.user.myacount.entity.VaultMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserVaultViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase
) : ViewModel() {

    private val _vaultItems = MutableStateFlow<List<VaultMenuItem>>(emptyList())
    val vaultItems: StateFlow<List<VaultMenuItem>> = _vaultItems.asStateFlow()

    private val _tripCount = MutableStateFlow(0)
    val tripCount: StateFlow<Int> = _tripCount.asStateFlow()

    private val _favoritesCount = MutableStateFlow(8)
    val favoritesCount: StateFlow<Int> = _favoritesCount.asStateFlow()

    private val _storiesCount = MutableStateFlow(34)
    val storiesCount: StateFlow<Int> = _storiesCount.asStateFlow()

    init {
        loadVaultData()
    }

    fun loadVaultData() {
        _vaultItems.value = ProfileActionResourceMapper.getVaultMenuItems()
        val user = authPreferencesUseCase.getLoggedUser()
        _tripCount.value = user?.tripCount?.toInt() ?: 0
    }
}
