package br.dev.saed.bioinformatica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.saed.bioinformatica.model.api.PythonAPI
import br.dev.saed.bioinformatica.model.api.RetrofitHelper
import br.dev.saed.bioinformatica.model.entities.Texto

class TextoViewModel : ViewModel() {
    private val retrofit = RetrofitHelper.retrofit.create(PythonAPI::class.java)
    private val _texto = MutableLiveData<Texto>()
    val texto: LiveData<Texto> get() = _texto

    suspend fun escreverTexto(texto: String) {
        try {
            val response = retrofit.escreverTexto(Texto(texto))
            if (response.isSuccessful) {
                _texto.postValue(Texto(response.body()?.texto ?: "Erro"))
            } else {
                _texto.postValue(Texto("Erro"))
            }

        } catch (e: Exception) {
            _texto.postValue(Texto(e.message ?: "Erro"))
        }
    }
}