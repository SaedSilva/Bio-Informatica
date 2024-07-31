package br.dev.saed.bioinformatica.model.api

import br.dev.saed.bioinformatica.model.entities.Pessoa
import br.dev.saed.bioinformatica.model.entities.Sexo
import br.dev.saed.bioinformatica.model.entities.Texto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PythonAPI {
    @POST("/pessoa")
    suspend fun gerarNomeAleatorio(@Body sexo: Sexo): Response<Pessoa>

    @POST("/texto")
    suspend fun escreverTexto(@Body texto: Texto): Response<Texto>
}