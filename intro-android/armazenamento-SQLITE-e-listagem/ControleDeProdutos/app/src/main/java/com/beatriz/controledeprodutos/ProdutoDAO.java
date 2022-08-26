package com.beatriz.controledeprodutos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ProdutoDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.write = dbHelper.getWritableDatabase();
        this.read = dbHelper.getReadableDatabase();
    }

    public void salvarProduto(Produto produto) {
        // mapear chave e valor
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", produto.getNome());
        contentValues.put("estoque", produto.getEstoque());
        contentValues.put("valor", produto.getValor());

         try {
             write.insert(DBHelper.TB_PRODUTO, null, contentValues);
             //write.close();
         } catch (Exception e) {
             Log.i("ERROR", "Erro ao salvar produto" + e.getMessage());
         }
    }

    public List<Produto> getListaProdutos() {
        List<Produto> produtoList = new ArrayList<>();
        String sql = "SELECT * FROM " + DBHelper.TB_PRODUTO + ";";
        // cursor vai percorrer o banco de dados
        try (Cursor cursor = read.rawQuery(sql, null)) {

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex("nome"));
                @SuppressLint("Range") int estoque = cursor.getInt(cursor.getColumnIndex("estoque"));
                @SuppressLint("Range") double valor = cursor.getDouble(cursor.getColumnIndex("valor"));

                Produto produto = new Produto();
                produto.setId(id);
                produto.setNome(nome);
                produto.setEstoque(estoque);
                produto.setValor(valor);

                produtoList.add(produto);
            }
        }

        return produtoList;
    }

    public void atualizarProduto(Produto produto) {
        ContentValues cv = new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("valor", produto.getValor());

        String where = "id=?";
        String[] args = {String.valueOf(produto.getId())};

        try {
            write.update(DBHelper.TB_PRODUTO,cv,where,args);
            //write.close();
        } catch (Exception e) {
            Log.i("ERROR", "Erro ao atualizar o produto" + e.getMessage());
        }

    }

    public void deletarProduto(Produto produto) {
        try {
            String[] args = {String.valueOf(produto.getId())};
            String where = "id=?";
            write.delete(DBHelper.TB_PRODUTO, where, args );
        } catch (Exception e) {
            Log.i("ERROR", "Erro ao excluir o produto" + e.getMessage());
        }
    }

}
