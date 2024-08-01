package br.dev.saed.bioinformatica.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.entity.Comando
import br.dev.saed.bioinformatica.model.repository.ComandoRepository
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH

class SSHViewModel : ViewModel() {
    private lateinit var ssh: ConnectionSSH
    private lateinit var repository: ComandoRepository

    private val _comandos = MutableLiveData<List<Comando>>()
    val comandos: LiveData<List<Comando>> get() = _comandos

    fun instanciarRepository(context: Context) {
        repository = ComandoRepository(context)
    }

    fun instaciarSSH(ssh: ConnectionSSH) {
        this.ssh = ssh
        ssh.connectSession()
    }

    fun connect(name: String, password: String, host: String, port: Int): Boolean {
        ssh = ConnectionSSH(name, password, host, port)
        return ssh.connectSession()
    }

    fun disconnect() {
        ssh.disconnectSession()
    }

    fun getSSH(): ConnectionSSH {
        return ssh
    }

    fun executeCommand(comando: Comando): String {
        return ssh.executeCommand(comando.comando)
    }

    fun carregarComandos() {
        _comandos.postValue(repository.listar())
    }

    fun salvarComando(comando: Comando) {
        repository.salvar(comando)
    }

    fun editarComando(comando: Comando) {
        repository.atualizar(comando)
    }

    fun excluirComando(comando: Comando) {
        repository.deletar(comando)
    }
}