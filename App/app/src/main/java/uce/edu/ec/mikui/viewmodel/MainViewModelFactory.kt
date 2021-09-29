package uce.edu.ec.mikui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uce.edu.ec.mikui.data.dominio.Repositorio

class MainViewModelFactory(private val repositorio: Repositorio): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repositorio) as T
    }

}