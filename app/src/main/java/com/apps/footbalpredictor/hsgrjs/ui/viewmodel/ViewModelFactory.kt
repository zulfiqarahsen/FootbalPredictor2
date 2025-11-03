package com.apps.footbalpredictor.hsgrjs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository

class ViewModelFactory(
    private val repository: FootballRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchSimulationViewModel::class.java)) {
            return MatchSimulationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


