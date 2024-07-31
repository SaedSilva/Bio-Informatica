package br.dev.saed.bioinformatica.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityMainBinding
import br.dev.saed.bioinformatica.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
    }

    private fun inicializarObservers() {

    }

    private fun inicializarComponentes() {
        binding.btnScript1.setOnClickListener {
            val intent = Intent(this, ScriptUmActivity::class.java)
            startActivity(intent)
        }
        binding.btnScript2.setOnClickListener {
            val intent = Intent(this, TextoActivity::class.java)
            startActivity(intent)
        }
        binding.btnScriptSSH.setOnClickListener {
            val intent = Intent(this, SSHActivity::class.java)
            startActivity(intent)
        }
    }
}