package br.dev.saed.bioinformatica.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comando(
    val id: Int?,
    val nome: String,
    val comando: String
) : Parcelable
