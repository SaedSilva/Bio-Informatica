package br.dev.saed.bioinformatica.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivitySalvarComandoBinding
import br.dev.saed.bioinformatica.model.entity.Comando
import br.dev.saed.bioinformatica.viewmodel.SSHViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SalvarComandoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalvarComandoBinding
    private val viewModel: SSHViewModel by viewModels()
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalvarComandoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        receberDados()
        viewModel.instanciarRepository(this)
    }

    private fun receberDados() {
        val extras = intent.extras
        if (extras != null) {
            val comando = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable("comando", Comando::class.java)
            } else {
                extras.getParcelable("comando") as Comando?
            }
            if (comando != null) {
                id = comando.id
                binding.etNome.setText(comando.nome)
                binding.etComando.setText(comando.comando)
            }
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
                    val result = viewModel.executeCommand(Comando(id, nome, comando))
                    val intent = Intent(applicationContext, ResultadoActivity::class.java)
                    intent.putExtra("result", result)
                    startActivity(intent)
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
                    if (id == null) {
                        viewModel.salvarComando(Comando(null, nome, comando))
                    } else {
                        viewModel.editarComando(Comando(id, nome, comando))
                    }
                }
                finish()
            }
        }
    }
}