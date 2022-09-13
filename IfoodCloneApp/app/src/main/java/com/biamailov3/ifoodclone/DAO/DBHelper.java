package com.biamailov3.ifoodclone.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DB_IFOOD";

    public static final String TABELA_EMPRESA = "empresa";
    public static final String TABELA_ITEM_PEDIDO = "item_pedido";
    public static final String TABELA_ENTREGA = "entrega";

    public static final String COLUNA_ID = "id";
    public static final String COLUNA_ID_FIREBASE = "id_firebase";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_VALOR = "valor";
    public static final String COLUNA_TEMPO_MINIMO = "tempo_minimo";
    public static final String COLUNA_TEMPO_MAXIMO = "tempo_maximo";
    public static final String COLUNA_URL_IMAGEM = "url_imagem";
    public static final String COLUNA_QUANTIDADE = "quantidade";
    public static final String COLUNA_TAXA_ENTREGA = "taxa_entrega";
    public static final String COLUNA_FORMA_PAGAMENTO = "forma_pagamento";
    public static final String COLUNA_ENDERECO_LOGRADOURO = "logradouro";
    public static final String COLUNA_ENDERECO_BAIRRO = "bairro";
    public static final String COLUNA_ENDERECO_REFERENCIA = "referencia";
    public static final String COLUNA_ENDERECO_MUNICIPIO = "municipio";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TABLE_EMPRESA = " CREATE TABLE IF NOT EXISTS " + TABELA_EMPRESA +
                " (id_firebase TEXT NOT NULL, " +
                " nome TEXT NOT NULL, " +
                " taxa_entrega DOUBLE NOT NULL, " +
                " tempo_minimo INTEGER NOT NULL, " +
                " tempo_maximo INTEGER NOT NULL, " +
                " url_imagem TEXT NOT NULL); ";

        String TABLE_ITEM_PEDIDO = " CREATE TABLE IF NOT EXISTS " + TABELA_ITEM_PEDIDO +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " id_firebase TEXT NOT NULL, " +
                " nome TEXT NOT NULL, " +
                " url_imagem TEXT NOT NULL, " +
                " valor DOUBLE NOT NULL, " +
                " quantidade INTEGER NOT NULL); ";

        String TABLE_ENTREGA = " CREATE TABLE IF NOT EXISTS " + TABELA_ENTREGA +
                " (forma_pagamento TEXT NOT NULL, " +
                " logradouro TEXT NOT NULL, " +
                " bairro TEXT NOT NULL, " +
                " referencia TEXT NOT NULL, " +
                " municipio TEXT NOT NULL); ";

        try {
            db.execSQL(TABLE_EMPRESA);
            db.execSQL(TABLE_ITEM_PEDIDO);
            db.execSQL(TABLE_ENTREGA);
            Log.i("INFO_DB", "onCreate: tabela criada com sucessooooo");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao criar tabelaaaaaaaaaa");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
