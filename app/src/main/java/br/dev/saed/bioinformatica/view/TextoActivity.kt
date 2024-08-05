package br.dev.saed.bioinformatica.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityTextoBinding
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.viewmodel.ScriptViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TextoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextoBinding
    private val viewModel: ScriptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        verificarConexao()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
    }

    private fun verificarConexao() {
        if (ConfigManager.config == null) {
            finish()
        }
        CoroutineScope(Dispatchers.IO).launch {
            async { viewModel.connect() }.await()
            if (!viewModel.isConnected()) {
                finish()
            }
        }
        viewModel.resultado.observe(this) { resultado ->
            if (!resultado) {
                finish()
            }
        }
    }

    private fun inicializarObservers() {
        viewModel.mensagem.observe(this) { mensagem ->
            binding.tvResultadoScript2.text = mensagem
        }
        viewModel.resultado.observe(this) { resultado ->
            Toast.makeText(
                this,
                if (resultado) "Conectado" else "Erro ao conectar",
                Toast.LENGTH_SHORT
            ).show()
            if (!resultado) {
                finish()
            }
        }
    }

    private fun inicializarComponentes() {
        Toast.makeText(this, "Conectando em: ${viewModel.getHostAndPort()}", Toast.LENGTH_SHORT)
            .show()

        binding.btnEnviarTexto.setOnClickListener {
            if (binding.etNumeroPalavras.text.toString().isEmpty()) {
                binding.etNumeroPalavras.error = "Digite uma mensagem"
            } else if (binding.etNumeroCaracteres.text.toString().isEmpty()) {
                binding.etNumeroCaracteres.error = "Digite uma mensagem"
            } else {
                val comando1 = binding.etNumeroCaracteres.text.toString()
                val comando2 = binding.etNumeroPalavras.text.toString()
                val comandoConcatenado = concatenarComandos(comando1, comando2)

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.send(comandoConcatenado)
                }
            }
        }
    }

    private fun concatenarComandos(comando1: String, comando2: String): String {
        return "texto, $comando1, $comando2"
    }
}