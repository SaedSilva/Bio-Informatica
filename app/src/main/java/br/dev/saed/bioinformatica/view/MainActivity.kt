package br.dev.saed.bioinformatica.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.R
import br.dev.saed.bioinformatica.databinding.ActivityMainBinding
import br.dev.saed.bioinformatica.model.utils.Config
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.model.utils.dataStore
import br.dev.saed.bioinformatica.model.utils.host
import br.dev.saed.bioinformatica.model.utils.porta
import br.dev.saed.bioinformatica.model.utils.timeout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
    }

    override fun onStart() {
        super.onStart()
        carregarConfiguracoes()
    }

    private fun carregarConfiguracoes() {
        dataStore.data.map { preferences ->
            val host = preferences[host]
            val porta = preferences[porta]
            val timeout = preferences[timeout]
            if ((host != null && porta != null && timeout != null) && (host.isNotBlank())) {
                ConfigManager.config = Config(host, porta, timeout)
            } else {
                val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setTitle("Configurações")
                    .setMessage("É necessário configurar o host e a porta para continuar.")
                    .setPositiveButton("OK") { _, _ ->
                    }

                    .show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white))
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white))
                alertDialog.setOnDismissListener {
                    val intent = Intent(this, ConfiguracoesActivity::class.java)
                    startActivity(intent)
                }
            }
        }.launchIn(lifecycleScope)
    }


    private fun inicializarComponentes() {
        binding.btnSocket.setOnClickListener {
            val intent = Intent(this, ScriptUmActivity::class.java)
            startActivity(intent)
        }

        binding.btnConfiguracoes.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }
    }
}