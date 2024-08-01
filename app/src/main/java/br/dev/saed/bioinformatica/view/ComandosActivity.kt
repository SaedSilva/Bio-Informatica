package br.dev.saed.bioinformatica.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.dev.saed.bioinformatica.R
import br.dev.saed.bioinformatica.databinding.ActivityComandosBinding
import br.dev.saed.bioinformatica.view.adapter.ComandosAdapterRV
import br.dev.saed.bioinformatica.viewmodel.SSHViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComandosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComandosBinding
    private val viewModel: SSHViewModel by viewModels()
    private lateinit var comandosAdapterRV: ComandosAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComandosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarComponentes()
        inicializarObservers()
        configurarRV()
    }

    override fun onResume() {
        super.onResume()
        viewModel.instanciarRepository(this)
        viewModel.carregarComandos()
    }

    private fun configurarRV() {
        comandosAdapterRV = ComandosAdapterRV(
            executar = { comando ->
                CoroutineScope(Dispatchers.IO).launch {
                    val result = viewModel.executeCommand(comando)
                    val intent = Intent(applicationContext, ResultadoActivity::class.java)
                    intent.putExtra("result", result)
                    startActivity(intent)
                }
            },
            editar = { comando ->
                val intent = Intent(applicationContext, SalvarComandoActivity::class.java)
                intent.putExtra("comando", comando)
                startActivity(intent)
            },
            excluir = { comando ->
                val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setMessage("Deseja excluir o comando?")
                    .setPositiveButton("Sim") { _, _ ->
                        viewModel.excluirComando(comando)
                        viewModel.carregarComandos()
                    }
                    .setNegativeButton("Não") { _, _ -> }
                    .show()

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getColor(R.color.white))
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getColor(R.color.white))
            }
        )
        binding.rvComandos.adapter = comandosAdapterRV
        binding.rvComandos.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
    }

    private fun inicializarObservers() {
        viewModel.comandos.observe(this) {
            comandosAdapterRV.atualizarComandos(it)
        }
    }

    private fun inicializarComponentes() {
        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, SalvarComandoActivity::class.java)
            startActivity(intent)
        }
    }
}