package br.dev.saed.bioinformatica.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "$TABLE_NAME", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE COMANDOS ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NOME TEXT, $COMANDO TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    companion object {
        const val TABLE_NAME = "COMANDOS"
        const val ID = "ID"
        const val NOME = "NOME"
        const val COMANDO = "COMANDO"
    }
}