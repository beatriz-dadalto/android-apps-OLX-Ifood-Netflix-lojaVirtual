package com.beatriz.controledeprodutos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String NOME_DB = "DB_APP";
    public static final String TB_PRODUTO = "TB_PRODUTO";

    public DBHelper(Context context) {
        super(context, NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_PRODUTO
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL, " +
                " estoque INTEGER NOT NULL, " +
                " valor DOUBLE NOT NULL); ";

        try {
            sqLiteDatabase.execSQL(sql);
        } catch (Exception e) {
            Log.i("ERROR", "Erro ao criar a tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
