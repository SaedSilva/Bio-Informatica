package br.dev.saed.bioinformatica.model.socket

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class SocketClient(
    host: String,
    port: Int,
    private var timeout: Int
) {
    private var client: Socket = Socket()
    private lateinit var inputStreamReader: InputStreamReader
    private lateinit var outputStreamWriter: OutputStreamWriter
    private var socketAddress: SocketAddress = InetSocketAddress(host, port)

    suspend fun connect(): Boolean {
        try {
            withContext(Dispatchers.IO) {
                client.connect(socketAddress, (timeout * 1000))
                inputStreamReader = client.getInputStream().reader()
                outputStreamWriter = client.getOutputStream().writer()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun send(message: String): String {
        try {
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(message)
            bufferedWriter.flush()

            val bufferedReader = BufferedReader(inputStreamReader)
            return bufferedReader.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
            return "Erro ao enviar mensagem"
        }
    }

    fun disconnect() {
        try {
            client.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean {
        return client.isConnected
    }
}