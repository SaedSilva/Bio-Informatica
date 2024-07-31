package br.dev.saed.bioinformatica.model.ssh

import android.os.Parcelable
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
class ConnectionSSH(
    private val username: String,
    private val password: String,
    private val host: String,
    private val port: Int,
): Parcelable {
    private val jsch = JSch()
    private val session: Session = jsch.getSession(username, host, port)

    fun connectSession(): Boolean {
        try {
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect()
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

        val channel = session.openChannel("exec") as ChannelExec
        val responseStream = ByteArrayOutputStream()
        try {
            channel.setCommand(command)
            channel.outputStream = responseStream
            channel.connect()

            while (!channel.isClosed) {
                Thread.sleep(1000)
            }

            return responseStream.toString()
        } catch (e: Exception) {
            return "Error: ${e.message}"
        } finally {
            if (channel.isConnected) channel.disconnect()
        }
    }
}