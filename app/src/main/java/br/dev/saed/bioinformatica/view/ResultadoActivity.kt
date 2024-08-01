package br.dev.saed.bioinformatica.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityResultadoBinding

class ResultadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receberDados()
        inicializarComponentes()
    }

    private fun inicializarComponentes() {

    }

    private fun receberDados() {
        val result = intent.getStringExtra("result")
        binding.tvResultado.text = result
    }
}