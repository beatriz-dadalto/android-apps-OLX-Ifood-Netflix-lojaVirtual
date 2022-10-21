package com.br.ecommerce.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.br.ecommerce.model.ImagemUpload;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;

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

    public boolean salvar(ItemPedido itemPedido) {

        ContentValues values = new ContentValues();
        values.put("id_produto", itemPedido.getIdProduto());
        values.put("valor", itemPedido.getValor());
        values.put("quantidade", itemPedido.getQuantidade());

        try {
            // criar a tabela no dispositivo
            write.insert(DBHelper.TABELA_ITEM_PEDIDO, null, values);
        } catch (Exception e) {
            Log.i("INFODB:", " Erro ao salvar o itemPedido." + e.getMessage());
            return false;
        }

        return true;
    }

    // recuperando do SQLite
    public Produto getProduto(int idProduto) {
        Produto produto = null;
        List<ImagemUpload> imagemUploadList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABELA_ITEM + " WHERE id = " + idProduto + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String id_firebase = cursor.getString(cursor.getColumnIndexOrThrow("id_firebase"));
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            double valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"));
            String url_imagem = cursor.getString(cursor.getColumnIndexOrThrow("url_imagem"));

            produto = new Produto();
            produto.setIdLocal(id);
            produto.setId(id_firebase);
            produto.setTitulo(titulo);
            produto.setValorAtual(valor);

            imagemUploadList.add(new ImagemUpload(0, url_imagem));

            produto.setUrlsImagens(imagemUploadList);
        }

        cursor.close();
        return produto;
    }
}
