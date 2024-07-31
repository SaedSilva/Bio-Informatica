package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH

class ComandosViewModel : ViewModel() {
    private lateinit var ssh: ConnectionSSH
    private val _comando = MutableLiveData<String>()
    val comando: LiveData<String> get() = _comando

    fun instantiateSSH(ssh: ConnectionSSH) {
        this.ssh = ssh
        ssh.connectSession()
    }

    fun executeCommand(command: String) {
        _comando.postValue(ssh.executeCommand(command))
    }

}