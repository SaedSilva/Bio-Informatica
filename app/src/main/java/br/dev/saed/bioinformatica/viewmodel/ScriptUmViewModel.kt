package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.api.PythonAPI
import br.dev.saed.bioinformatica.model.api.RetrofitHelper
import br.dev.saed.bioinformatica.model.entities.Pessoa
import br.dev.saed.bioinformatica.model.entities.Sexo

class ScriptUmViewModel : ViewModel() {
    private val retrofit = RetrofitHelper.retrofit.create(PythonAPI::class.java)
    private val _pessoa = MutableLiveData<Pessoa>()
    val pessoa: LiveData<Pessoa> get() = _pessoa

    suspend fun gerarPessoa(sexo: Sexo) {
        try {
            val response = retrofit.gerarNomeAleatorio(sexo)
            if (response.isSuccessful) {
                _pessoa.postValue(response.body())
            } else {
                _pessoa.postValue(Pessoa("Erro"))
            }
        } catch (e: Exception) {
            _pessoa.postValue(Pessoa(e.message ?: "Erro"))
        }
    }
}