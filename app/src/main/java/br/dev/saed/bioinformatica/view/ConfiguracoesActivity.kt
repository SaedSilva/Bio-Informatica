package br.dev.saed.bioinformatica.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.databinding.ActivityConfiguracoesBinding
import br.dev.saed.bioinformatica.model.utils.Config
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.model.utils.dataStore
import br.dev.saed.bioinformatica.model.utils.host
import br.dev.saed.bioinformatica.model.utils.porta
import br.dev.saed.bioinformatica.model.utils.timeout
import br.dev.saed.bioinformatica.viewmodel.ScriptViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfiguracoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracoesBinding
    private val viewModel: ScriptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        carregarConfiguracoes()
    }

    private fun carregarConfiguracoes() {
        dataStore.data.map { preferences ->
            val host = preferences[host] ?: ""
            val porta = preferences[porta] ?: 0
            val timeout = preferences[timeout] ?: 1

            ConfigManager.config = Config(host, porta.toString().toInt(), timeout)

            binding.etHost.setText(host)
            binding.etPorta.setText(porta.toString())
            binding.etTimeout.setText(timeout.toString())
        }.launchIn(lifecycleScope)
    }

    private fun inicializarObservers() {
        viewModel.resultado.observe(this) {
            if (it) {
                Toast.makeText(this, "Conectado com sucesso", Toast.LENGTH_SHORT).show()
                viewModel.disconnect()
            } else {
                Toast.makeText(this, "Erro ao conectar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inicializarComponentes() {
        binding.btnSalvarConfig.setOnClickListener {
            salvarConfiguracoes()
            finish()
        }
        binding.btnTestarConfig.setOnClickListener {
            Toast.makeText(
                this,
                "Tentando conectar em ${viewModel.getHostAndPort()}",
                Toast.LENGTH_SHORT
            ).show()

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.connect()
            }
        }
    }

    private fun salvarConfiguracoes() {
        ConfigManager.config = Config(
            binding.etHost.text.toString(),
            binding.etPorta.text.toString().toInt(),
            binding.etTimeout.text.toString().toInt()
        )
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[host] = binding.etHost.text.toString()
                preferences[porta] = binding.etPorta.text.toString().toInt()
                preferences[timeout] = binding.etTimeout.text.toString().toInt()
            }
        }
    }
}