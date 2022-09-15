package com.biamailov3.ifoodclone.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biamailov3.ifoodclone.model.ItemPedido;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ItemPedidoDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    public void salvar(ItemPedido itemPedido) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_ID_FIREBASE, itemPedido.getIdItem());
        cv.put(DBHelper.COLUNA_NOME, itemPedido.getItem());
        cv.put(DBHelper.COLUNA_URL_IMAGEM, itemPedido.getUrlImagem());
        cv.put(DBHelper.COLUNA_VALOR, itemPedido.getValor());
        cv.put(DBHelper.COLUNA_QUANTIDADE, itemPedido.getQuantidade());

        try {
            write.insert(DBHelper.TABELA_ITEM_PEDIDO, null, cv);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao salvar a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao salvar a tabelaaaa");
        }
    }

    public void atualizar(ItemPedido itemPedido) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_QUANTIDADE, itemPedido.getQuantidade());

        try {
            String where = "id=?";
            String[] args = {String.valueOf(itemPedido.getId())};
            write.update(DBHelper.TABELA_ITEM_PEDIDO, cv, where, args);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao atualizar a tabelaaaa");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao atualizar a tabelaaaa");
        }
    }

    public List<ItemPedido> getList() {
        List<ItemPedido> itemPedidoList = new ArrayList<>();

        String sql = " SELECT * FROM " + DBHelper.TABELA_ITEM_PEDIDO + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") Long id_local = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUNA_ID));
            @SuppressLint("Range") String id_firebase = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_ID_FIREBASE));
            @SuppressLint("Range") String item_nome = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_NOME));
            @SuppressLint("Range") String url_imagem = cursor.getString(cursor.getColumnIndex(DBHelper.COLUNA_URL_IMAGEM));
            @SuppressLint("Range") double valor = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUNA_VALOR));
            @SuppressLint("Range") int quantidade = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUNA_QUANTIDADE));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setId(id_local);
            itemPedido.setIdItem(id_firebase);
            itemPedido.setItem(item_nome);
            itemPedido.setUrlImagem(url_imagem);
            itemPedido.setValor(valor);
            itemPedido.setQuantidade(quantidade);

            itemPedidoList.add(itemPedido);
        }

        return itemPedidoList;
    }

    public Double getTotal() {
        double total = 0.0;
        for (ItemPedido itemPedido : getList()) {
            total += itemPedido.getValor() * itemPedido.getQuantidade();
        }

        return total;
    }

    public void remover(Long id) {
        try {
            String where = "id=?";
            String[] args = {String.valueOf(id)};
            write.delete(DBHelper.TABELA_ITEM_PEDIDO, where, args);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao remover item");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao remover item");
        }
    }

    public void removerTodos(Long id) {
        try {
            write.delete(DBHelper.TABELA_ITEM_PEDIDO, null, null);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao remover todos os itens");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao remover todos os itens");
        }
    }

    public void limparCarrinho() {
        try {
            write.delete(DBHelper.TABELA_EMPRESA, null, null);
            write.delete(DBHelper.TABELA_ENTREGA, null, null);
            write.delete(DBHelper.TABELA_ITEM_PEDIDO, null, null);
            Log.i("INFO_DB", "onCreate: Sucessoooo ao limpar o carrinho");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao limpar o carrinho");
        }
    }

}
