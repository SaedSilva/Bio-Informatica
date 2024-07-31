package br.dev.saed.bioinformatica.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.databinding.ActivitySshactivityBinding
import br.dev.saed.bioinformatica.viewmodel.SSHViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SSHActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySshactivityBinding
    private val viewModel: SSHViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySshactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
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
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Erro ao conectar", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}