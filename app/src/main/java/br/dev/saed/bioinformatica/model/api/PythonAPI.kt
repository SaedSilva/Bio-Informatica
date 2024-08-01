package br.dev.saed.bioinformatica.model.api

import br.dev.saed.bioinformatica.model.entity.Pessoa
import br.dev.saed.bioinformatica.model.entity.Sexo
import br.dev.saed.bioinformatica.model.entity.Texto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PythonAPI {
    @POST("/pessoa")
    suspend fun gerarNomeAleatorio(@Body sexo: Sexo): Response<Pessoa>

    @POST("/texto")
    suspend fun escreverTexto(@Body texto: Texto): Response<Texto>
}