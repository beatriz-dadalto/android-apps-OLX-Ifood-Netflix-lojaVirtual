package com.biamailov3.ifoodclone.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.EntregaPedido;

public class EntregaDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public EntregaDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    public void salvarEndereco(Endereco endereco) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_FORMA_PAGAMENTO, "");
        cv.put(DBHelper.COLUNA_ENDERECO_LOGRADOURO, endereco.getLogradouro());
        cv.put(DBHelper.COLUNA_ENDERECO_BAIRRO, endereco.getBairro());
        cv.put(DBHelper.COLUNA_ENDERECO_MUNICIPIO, endereco.getMunicipio());
        cv.put(DBHelper.COLUNA_ENDERECO_REFERENCIA, endereco.getReferencia());

        try {
            write.insert(DBHelper.TABELA_ENTREGA, null, cv);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao salvar a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao salvar a tabelaaaa");
        }
    }

    public void atualizarEndereco(Endereco endereco) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_ENDERECO_LOGRADOURO, endereco.getLogradouro());
        cv.put(DBHelper.COLUNA_ENDERECO_BAIRRO, endereco.getBairro());
        cv.put(DBHelper.COLUNA_ENDERECO_MUNICIPIO, endereco.getMunicipio());
        cv.put(DBHelper.COLUNA_ENDERECO_REFERENCIA, endereco.getReferencia());

        try {
            write.update(DBHelper.TABELA_ENTREGA, cv, null, null);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao atualizar o endereçoo");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao atualizar o endereçoo");
        }
    }

    public void salvarPagamento(String formaPagamento) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_FORMA_PAGAMENTO, formaPagamento);

        try {
            write.update(DBHelper.TABELA_ENTREGA, cv, null, null);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao atualizar a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao atualizar a tabelaaaa");
        }
    }

    public EntregaPedido getEntrega() {
        EntregaPedido entregaPedido = new EntregaPedido();
        Endereco endereco = new Endereco();

        String sql = " SELECT * FROM " + DBHelper.TABELA_ENTREGA + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String forma_pagamento = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_FORMA_PAGAMENTO));
            @SuppressLint("Range") String logradouro = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ENDERECO_LOGRADOURO));
            @SuppressLint("Range") String bairro = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ENDERECO_BAIRRO));
            @SuppressLint("Range") String municipio = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ENDERECO_MUNICIPIO));
            @SuppressLint("Range") String referencia = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ENDERECO_REFERENCIA));

            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setMunicipio(municipio);
            endereco.setReferencia(referencia);

            entregaPedido.setFormaPagamento(forma_pagamento);
            entregaPedido.setEndereco(endereco);
        }

        return entregaPedido;
    }

}
