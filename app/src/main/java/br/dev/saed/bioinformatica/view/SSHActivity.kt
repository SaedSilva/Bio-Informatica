package br.dev.saed.bioinformatica.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.databinding.ActivitySshactivityBinding
import br.dev.saed.bioinformatica.viewmodel.SSHViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ssh_preferences")
private val usuario = stringPreferencesKey("usuario")
private val senha = stringPreferencesKey("senha")
private val host = stringPreferencesKey("host")
private val porta = intPreferencesKey("porta")

class SSHActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySshactivityBinding
    private val viewModel: SSHViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySshactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        carregarConfiguracoes()
    }

    private fun carregarConfiguracoes() {
        dataStore.data.map { preferences ->
                val usuario = preferences[usuario] ?: ""
                val senha = preferences[senha] ?: ""
                val host = preferences[host] ?: ""
                val porta = preferences[porta] ?: ""
                binding.etUsuario.setText(usuario)
                binding.etSenha.setText(senha)
                binding.etHost.setText(host)
                binding.etPorta.setText(porta.toString())
            }
            .launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            viewModel.disconnect()
        }
    }

    private fun inicializarObservers() {

    }

    private fun inicializarComponentes() {
        binding.btnConectar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (viewModel.connect(
                        binding.etUsuario.text.toString(),
                        binding.etSenha.text.toString(),
                        binding.etHost.text.toString(),
                        binding.etPorta.text.toString().toInt()
                    )
                ) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Conectado", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(applicationContext, ComandosActivity::class.java)
                    intent.putExtra("ssh", viewModel.getSSH())
                    startActivity(intent)

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Erro ao conectar", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            salvarConfiguracoes()
        }
    }

    private fun salvarConfiguracoes() {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[usuario] = binding.etUsuario.text.toString()
                preferences[senha] = binding.etSenha.text.toString()
                preferences[host] = binding.etHost.text.toString()
                preferences[porta] = binding.etPorta.text.toString().toInt()
            }
        }
    }
}