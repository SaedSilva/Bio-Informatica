package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.entity.Comando

class ConfiguracoesViewModel : ViewModel() {

    private val _comandos = MutableLiveData<List<Comando>>()
    val comandos: LiveData<List<Comando>> get() = _comandos

}