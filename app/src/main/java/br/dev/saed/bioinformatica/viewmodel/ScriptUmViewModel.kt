package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.bioinformatica.model.socket.SocketClient
import br.dev.saed.bioinformatica.model.socket.SocketManager
import br.dev.saed.bioinformatica.model.utils.ConfigManager

class ScriptUmViewModel : ViewModel() {

    private val uiScope = viewModelScope

    private val _mensagem = MutableLiveData<String>()
    val mensagem: LiveData<String> get() = _mensagem

    private val _resultado = MutableLiveData<Boolean>()
    val resultado: LiveData<Boolean> get() = _resultado

    suspend fun connect(): Boolean {
        SocketManager.socketClient =
            SocketClient(ConfigManager.config!!.host, ConfigManager.config!!.port)
        val result = SocketManager.socketClient!!.connect()
        _resultado.postValue(result)
        return result
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