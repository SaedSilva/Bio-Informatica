package br.dev.saed.bioinformatica.view

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivitySalvarComandoBinding
import br.dev.saed.bioinformatica.model.entity.Comando
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH
import br.dev.saed.bioinformatica.viewmodel.SalvarComandoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SalvarComandoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalvarComandoBinding
    private val viewModel: SalvarComandoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalvarComandoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        receberDados()
    }

    private fun receberDados() {
        val extras = intent.extras
        if (extras != null) {
            val ssh = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable("ssh", ConnectionSSH::class.java)
            } else {
                extras.getParcelable("ssh")
            }
            if (ssh != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.instantiateSSH(ssh)
                }
            } else {
                finish()
            }
        }
    }

    private fun inicializarObservers() {
        viewModel.comando.observe(this) {
            binding.tvResultado.setText(it)
        }
    }

    private fun inicializarComponentes() {
        binding.btnExecutar.setOnClickListener {
            val nome = binding.etNome.text.toString()
            val comando = binding.etComando.text.toString()

            if (nome.isEmpty()) {
                binding.etNome.error = "* Obrigatório"
            } else if (comando.isEmpty()) {
                binding.etComando.error = "* Obrigatório"
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.executeCommand(Comando(null, nome, comando))
                }
            }
        }
        binding.btnSalvar.setOnClickListener {
            val nome = binding.etNome.text.toString()
            val comando = binding.etComando.text.toString()

            if (nome.isEmpty()) {
                binding.etNome.error = "* Obrigatório"
            } else if (comando.isEmpty()) {
                binding.etComando.error = "* Obrigatório"
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.salvarComando(Comando(null, nome, comando), applicationContext)
                }
            }
        }
    }
}