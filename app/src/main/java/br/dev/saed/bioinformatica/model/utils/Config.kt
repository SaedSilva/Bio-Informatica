package br.dev.saed.bioinformatica.model.utils

object ConfigManager {
    var config: Config? = null
}

class Config(
    var host: String,
    var port: Int,
    var timeout: Int
)

