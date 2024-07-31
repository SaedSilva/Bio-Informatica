package br.dev.saed.bioinformatica.model.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.io.ByteArrayOutputStream

class ConnectionSSH(
    private val username: String,
    private val password: String,
    private val host: String,
    private val port: Int,
) {
    private val jsch = JSch()
    private val session: Session = jsch.getSession(username, host, port)

    private lateinit var channel: ChannelExec

    fun connectSession(): Boolean {
        try {
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect()
            channel = session.openChannel("exec") as ChannelExec
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun disconnectSession() {
        if (session.isConnected) session.disconnect()
    }

    fun executeCommand(command: String): String {
        if (!session.isConnected) return "Session is not connected"

        channel.setCommand(command)
        val responseStream = ByteArrayOutputStream()
        channel.outputStream = responseStream
        channel.connect()

        while (!channel.isClosed) {
            Thread.sleep(1000)
        }

        val result = String(responseStream.toByteArray())
        channel.disconnect()
        return result
    }
}