package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH

class SSHViewModel : ViewModel() {
    private lateinit var ssh: ConnectionSSH

    fun connect(name: String, password: String, host: String, port: Int): Boolean {
        ssh = ConnectionSSH(name, password, host, port)
        return ssh.connectSession()
    }

    fun disconnect() {
        ssh.disconnectSession()
    }
}