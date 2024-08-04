package br.dev.saed.bioinformatica.model.socket

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketClient(
    private val host: String,
    private val port: Int,
) {
    private lateinit var client: Socket
    private lateinit var inputStreamReader: InputStreamReader
    private lateinit var outputStreamWriter: OutputStreamWriter

    suspend fun connect(): Boolean {
        try {
            withContext(Dispatchers.IO) {
                client = Socket(host, port)
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