package com.br.ecommerce.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;

public class ItemDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ItemDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    public long salvar(Produto produto) {
        long idRetorno = 0L;

        ContentValues values = new ContentValues();
        values.put("id_firebase", produto.getId());
        values.put("nome", produto.getTitulo());
        values.put("valor", produto.getValorAtual());

        for (int i = 0; i < produto.getUrlsImagens().size(); i++) {
            if (produto.getUrlsImagens().get(i).getIndex() == 0) {
                values.put("url_imagem", produto.getUrlsImagens().get(i).getCaminhoImagem());
            }
        }

        try {
            // criar a tabela no dispositivo
            idRetorno = write.insert(DBHelper.TABELA_ITEM, null, values);
        } catch (Exception e) {
            Log.i("INFODB:", " Erro ao salvar o item." + e.getMessage());
        }

        return idRetorno;
    }

    public boolean remover(ItemPedido itemPedido) {

        String WHERE = "id=?";
        String[] args = {String.valueOf(itemPedido.getId())};

        try {
            // atualizar a tabela no dispositivo
            write.delete(DBHelper.TABELA_ITEM, WHERE, args);
            Log.i("INFODB:", " Sucesso ao remover o itemPedido.");
        } catch (Exception e) {
            Log.i("INFODB:", " Erro ao remover o itemPedido." + e.getMessage());
            return false;
        }

        return true;
    }
}
