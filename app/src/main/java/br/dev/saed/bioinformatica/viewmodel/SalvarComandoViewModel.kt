package br.dev.saed.bioinformatica.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.entity.Comando
import br.dev.saed.bioinformatica.model.repository.ComandoRepository
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH

class SalvarComandoViewModel : ViewModel() {

    private lateinit var ssh: ConnectionSSH
    private val _comando = MutableLiveData<String>()
    val comando: LiveData<String> get() = _comando

    fun instantiateSSH(ssh: ConnectionSSH) {
        this.ssh = ssh
        ssh.connectSession()
    }

    fun executeCommand(comando: Comando) {
        _comando.postValue(ssh.executeCommand(comando.comando))
    }

    fun salvarComando(comando: Comando, context: Context) {
        val repository = ComandoRepository(context)
        repository.salvar(comando)
    }
}