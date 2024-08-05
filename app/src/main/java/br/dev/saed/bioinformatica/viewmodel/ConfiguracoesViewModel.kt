package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfiguracoesViewModel : ViewModel() {
    private val _resultado = MutableLiveData<String>()
    val resultado: LiveData<String> = _resultado


}