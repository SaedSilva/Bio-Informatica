package br.dev.saed.bioinformatica.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.dev.saed.bioinformatica.databinding.ActivityComandosBinding
import br.dev.saed.bioinformatica.model.ssh.ConnectionSSH
import br.dev.saed.bioinformatica.view.adapter.ComandosAdapterRV
import br.dev.saed.bioinformatica.viewmodel.ComandosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComandosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComandosBinding
    private val comandosViewModel: ComandosViewModel by viewModels()
    private lateinit var comandosAdapterRV: ComandosAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComandosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        receberDados()
        configurarRV()
        comandosViewModel.carregarComandos(applicationContext)
    }

    private fun configurarRV() {
        comandosAdapterRV = ComandosAdapterRV(
            executar = { comando ->
                CoroutineScope(Dispatchers.IO).launch {
                    comandosViewModel.executeCommand(comando.comando)
                }
            },
            editar = { comando ->

            },
            excluir = { comando ->
                CoroutineScope(Dispatchers.IO).launch {

                }
            }
        )
        binding.rvComandos.adapter = comandosAdapterRV
        binding.rvComandos.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
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
        comandosViewModel.comandos.observe(this) {
            comandosAdapterRV.atualizarComandos(it)
        }
    }

    private fun inicializarComponentes() {
        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, SalvarComandoActivity::class.java)
            intent.putExtra("ssh", comandosViewModel.getSSH())
            startActivity(intent)
        }
    }
}