package br.dev.saed.bioinformatica.view

import android.os.Bundle
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
import br.dev.saed.bioinformatica.viewmodel.ConfiguracoesViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfiguracoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracoesBinding
    private val viewModel: ConfiguracoesViewModel by viewModels()

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
            val porta = preferences[porta] ?: "0"

            ConfigManager.config = Config(host, porta.toString().toInt())

            binding.etHost.setText(host)
            binding.etPorta.setText(porta.toString())
        }.launchIn(lifecycleScope)
    }

    private fun inicializarObservers() {

    }

    private fun inicializarComponentes() {
        binding.btnSalvarConfig.setOnClickListener {
            salvarConfiguracoes()
            finish()
        }
    }

    private fun salvarConfiguracoes() {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[host] = binding.etHost.text.toString()
                preferences[porta] = binding.etPorta.text.toString().toInt()
                ConfigManager.config = Config(binding.etHost.text.toString(), binding.etPorta.text.toString().toInt())
            }
        }
    }
}