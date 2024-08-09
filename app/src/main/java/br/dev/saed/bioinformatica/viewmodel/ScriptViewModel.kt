package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.bioinformatica.model.socket.SocketClient
import br.dev.saed.bioinformatica.model.socket.SocketManager
import br.dev.saed.bioinformatica.model.utils.Config
import br.dev.saed.bioinformatica.model.utils.ConfigManager

class ScriptViewModel : ViewModel() {

    private val _mensagem = MutableLiveData<String>()
    val mensagem: LiveData<String> get() = _mensagem

    private val _resultado = MutableLiveData<Boolean>()
    val resultado: LiveData<Boolean> get() = _resultado

    private val _hostAndPort = MutableLiveData<String>()
    val hostAndPort: LiveData<String> get() = _hostAndPort

    fun getHostAndPort(): String {
        return "${ConfigManager.config!!.host}:${ConfigManager.config!!.port}"
    }

    suspend fun connect(): Boolean {
        SocketManager.socketClient =
            SocketClient(ConfigManager.config!!.host, ConfigManager.config!!.port, ConfigManager.config!!.timeout)
        val result = SocketManager.socketClient!!.connect()
        _resultado.postValue(result)
        return result
    }

    fun salvarConfiguracoes(host: String, port: Int, timeout: Int) {
        ConfigManager.config = Config(host = host, port = port, timeout = timeout)
    }

    fun send(mensagem: String) {
        _mensagem.postValue(SocketManager.socketClient!!.send(mensagem))
    }

    fun disconnect() {
        SocketManager.socketClient?.disconnect()
    }

    fun isConnected(): Boolean {
        return SocketManager.socketClient?.isConnected() ?: false
    }
}