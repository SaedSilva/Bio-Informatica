package br.dev.saed.bioinformatica.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.dev.saed.bioinformatica.databinding.ItemComandoBinding
import br.dev.saed.bioinformatica.model.entity.Comando

class ComandosAdapterRV(
    private val executar: (Comando) -> Unit,
    private val editar: (Comando) -> Unit,
    private val excluir: (Comando) -> Unit,
) : Adapter<ComandosAdapterRV.ComandoViewHolder>() {

    private var comandos:List<Comando> = mutableListOf()

    fun atualizarComandos(comandos: List<Comando>) {
        this.comandos = comandos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComandoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemComandoBinding.inflate(layoutInflater, parent, false)
        return ComandoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComandoViewHolder, position: Int) {
        val comando = comandos[position]
        holder.bind(comando)
    }

    override fun getItemCount(): Int {
        return comandos.size
    }

    inner class ComandoViewHolder(
        private val binding: ItemComandoBinding,
    ) : ViewHolder(binding.root) {
        fun bind(comando: Comando) {
            binding.tvNomeComando.text = comando.nome
            binding.tvComando.text = comando.comando

            binding.btnIniciar.setOnClickListener {
                executar(comando)
            }

            binding.btnEditar.setOnClickListener {
                editar(comando)
            }

            binding.btnDeletar.setOnClickListener {
                excluir(comando)
            }
        }
    }
}