package br.dev.saed.bioinformatica.model.ssh

class TestSSH {
}

fun main() {
    val command = "dir"
    val ssh = ConnectionSSH("saed", "3546", "127.0.0.1", 22)
    ssh.connectSession()
    val result = ssh.executeCommand(command)
    println(result)
    ssh.disconnectSession()
}