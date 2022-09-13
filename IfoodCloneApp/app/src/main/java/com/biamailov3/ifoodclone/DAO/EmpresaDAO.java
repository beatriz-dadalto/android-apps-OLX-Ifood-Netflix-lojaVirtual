package com.biamailov3.ifoodclone.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biamailov3.ifoodclone.model.Empresa;

public class EmpresaDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public EmpresaDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    public void salvar(Empresa empresa) {

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_ID_FIREBASE, empresa.getId());
        cv.put(DBHelper.COLUNA_NOME, empresa.getNome());
        cv.put(DBHelper.COLUNA_TAXA_ENTREGA, empresa.getTaxaEntrega());
        cv.put(DBHelper.COLUNA_TEMPO_MINIMO, empresa.getTempoMinEntrega());
        cv.put(DBHelper.COLUNA_TEMPO_MAXIMO, empresa.getTempoMaxEntrega());
        cv.put(DBHelper.COLUNA_URL_IMAGEM, empresa.getUrlLogo());

        try {
            write.insert(DBHelper.TABELA_EMPRESA, null, cv);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao salvar a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao salvar a tabelaaaa");
        }
    }

    public Empresa getEmpresa() {
        Empresa empresa = new Empresa();

        String sql = " SELECT * FROM " + DBHelper.TABELA_EMPRESA + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id_firebase = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ID_FIREBASE));
            @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_NOME));
            @SuppressLint("Range") double taxa_entrega = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUNA_TAXA_ENTREGA));
            @SuppressLint("Range") int tempo_minimo = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUNA_TEMPO_MINIMO));
            @SuppressLint("Range") int tempo_maximo = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUNA_TEMPO_MAXIMO));
            @SuppressLint("Range") String url_logo = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_URL_IMAGEM));

            empresa.setId(id_firebase);
            empresa.setNome(nome);
            empresa.setTaxaEntrega(taxa_entrega);
            empresa.setTempoMinEntrega(tempo_minimo);
            empresa.setTempoMaxEntrega(tempo_maximo);
            empresa.setUrlLogo(url_logo);
        }

        return empresa;
    }

    public void removerEmpresa() {

        try {
            write.delete(DBHelper.TABELA_EMPRESA, null, null);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao remover a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao remover a tabelaaaa");
        }

    }

}
