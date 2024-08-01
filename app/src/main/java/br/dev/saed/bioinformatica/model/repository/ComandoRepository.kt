package br.dev.saed.bioinformatica.model.repository

import android.content.Context
import br.dev.saed.bioinformatica.model.db.DatabaseHelper
import br.dev.saed.bioinformatica.model.entity.Comando

class ComandoRepository(context: Context) {
    private val write = DatabaseHelper(context).writableDatabase
    private val read = DatabaseHelper(context).readableDatabase

    fun salvar(comando: Comando): Boolean {
        return try {
            write.execSQL("INSERT INTO ${DatabaseHelper.TABLE_NAME} VALUES (null, '${comando.nome}', '${comando.comando}');")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun atualizar (comando: Comando): Boolean {
        return try {
            write.execSQL("UPDATE ${DatabaseHelper.TABLE_NAME} SET ${DatabaseHelper.NOME} = '${comando.nome}', ${DatabaseHelper.COMANDO} = '${comando.comando}' WHERE ${DatabaseHelper.ID} = ${comando.id};")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun deletar(comando: Comando): Boolean {
        return try {
            write.execSQL("DELETE FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.ID} = ${comando.id};")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun listar(): List<Comando> {
        return try {
            val cursor = read.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
            val result: MutableList<Comando> = mutableListOf()
            while (cursor.moveToNext()) {
                result.add(Comando(cursor.getInt(0), cursor.getString(1), cursor.getString(2)))
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }
}