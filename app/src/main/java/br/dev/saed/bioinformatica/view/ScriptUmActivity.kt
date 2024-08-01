package br.dev.saed.bioinformatica.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.databinding.ActivityScriptUmBinding
import br.dev.saed.bioinformatica.model.entity.Sexo
import br.dev.saed.bioinformatica.viewmodel.ScriptUmViewModel
import kotlinx.coroutines.launch

class ScriptUmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScriptUmBinding
    private val scriptUmViewModel: ScriptUmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriptUmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()

    }

    private fun inicializarObservers() {
        scriptUmViewModel.pessoa.observe(this) { pessoa ->
            binding.tvPessoa.text = pessoa.nome
        }
    }

    private fun inicializarComponentes() {
        binding.btnGerarPessoa.setOnClickListener {
            binding.rgScript1.checkedRadioButtonId.let { id ->
                val sexo = when (id) {
                    binding.rbFeminino.id -> Sexo("Feminino")
                    binding.rbMasculino.id -> Sexo("Masculino")
                    else -> Sexo("Masculino")
                }
                lifecycleScope.launch {
                    scriptUmViewModel.gerarPessoa(sexo)
                }
            }
        }
    }
}