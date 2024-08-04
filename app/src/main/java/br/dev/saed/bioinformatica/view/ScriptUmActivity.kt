package br.dev.saed.bioinformatica.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityScriptUmBinding
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.viewmodel.ScriptUmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScriptUmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScriptUmBinding
    private val viewModel: ScriptUmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriptUmBinding.inflate(layoutInflater)
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
            binding.tvResultadoScript1.text = mensagem
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
        binding.btnEnviar.setOnClickListener {
            if (binding.etMensagem.text.toString().isEmpty()) {
                binding.etMensagem.error = "Digite uma mensagem"
            } else if (binding.etMensagem2.text.toString().isEmpty()) {
                binding.etMensagem2.error = "Digite uma mensagem"
            } else {
                val comando1 = binding.etMensagem.text.toString()
                val comando2 = binding.etMensagem2.text.toString()
                val comandoConcatenado = concatenarComandos(comando1, comando2)

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.send(comandoConcatenado)
                }
            }
        }
    }

    private fun concatenarComandos(comando1: String, comando2: String): String {
        return "$comando1, $comando2"
    }
}