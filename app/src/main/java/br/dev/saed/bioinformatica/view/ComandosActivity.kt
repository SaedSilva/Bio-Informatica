package br.dev.saed.bioinformatica.view

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.dev.saed.bioinformatica.databinding.ActivityComandosBinding
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH
import br.dev.saed.bioinformatica.viewmodel.ComandosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComandosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComandosBinding
    private val comandosViewModel: ComandosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComandosBinding.inflate(layoutInflater)
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
                    comandosViewModel.instantiateSSH(ssh)
                }
            } else {
                finish()
            }
        }
    }

    private fun inicializarObservers() {
        comandosViewModel.comando.observe(this) {
            binding.tvResultadoComando.text = it
        }
    }

    private fun inicializarComponentes() {
        binding.btnExecutarComando.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                comandosViewModel.executeCommand(binding.etComando.text.toString())
            }
        }
    }
}