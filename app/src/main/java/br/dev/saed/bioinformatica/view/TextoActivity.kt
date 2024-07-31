package br.dev.saed.bioinformatica.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.dev.saed.bioinformatica.databinding.ActivityTextoBinding
import br.dev.saed.bioinformatica.viewmodel.TextoViewModel
import kotlinx.coroutines.launch

class TextoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextoBinding
    private val textoViewModel: TextoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
    }

    private fun inicializarObservers() {
        textoViewModel.texto.observe(this) {
            binding.textResultadoSalvar.text = it.texto
        }
    }

    private fun inicializarComponentes() {
        binding.btnSalvar.setOnClickListener {
            lifecycleScope.launch {
                val texto = binding.editTexto.text.toString()
                textoViewModel.escreverTexto(texto)
            }
        }
    }
}