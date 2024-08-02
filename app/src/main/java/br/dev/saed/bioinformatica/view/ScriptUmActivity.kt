package br.dev.saed.bioinformatica.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityScriptUmBinding
import br.dev.saed.bioinformatica.model.socket.SocketManager
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.viewmodel.ScriptUmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScriptUmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScriptUmBinding
    private val viewModel: ScriptUmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriptUmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
    }

    private fun inicializarObservers() {
        viewModel.mensagem.observe(this) { mensagem ->
            binding.tvResultadoScript1.text = mensagem
        }
        viewModel.resultado.observe(this) { resultado ->
            Toast.makeText(this, if (resultado) "Conectado" else "Erro ao conectar", Toast.LENGTH_SHORT).show()
            if (!resultado) {
                finish()
            }
        }
    }

    private fun inicializarComponentes() {
        if (ConfigManager.config == null) {
            finish()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { viewModel.connect() }.await()
            if (!result) {
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }

        if (SocketManager.socketClient == null) {
            finish()
        }

        binding.btnEnviar.setOnClickListener {
            if (binding.etMensagem.text.toString().isEmpty()) {
                binding.etMensagem.error = "Digite uma mensagem"
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.send(binding.etMensagem.text.toString())
                }
            }
        }
    }
}