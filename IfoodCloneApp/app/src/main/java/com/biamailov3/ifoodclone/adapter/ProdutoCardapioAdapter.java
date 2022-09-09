package com.biamailov3.ifoodclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.empresa.EmpresaProdutoDetalhesActivity;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProdutoCardapioAdapter extends RecyclerView.Adapter<ProdutoCardapioAdapter.MyViewHolder> {

    private List<Produto> produtoList;
    private Activity activity;

    public ProdutoCardapioAdapter(List<Produto> produtoList, Activity activity) {
        this.produtoList = produtoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto_cardapio, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtoList.get(position);

        Picasso.get().load(produto.getUrlImagem()).into(holder.imagemProduto);
        holder.textProdutoNome.setText(produto.getNome());
        holder.textProdutoValor.setText(activity.getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, EmpresaProdutoDetalhesActivity.class);
            intent.putExtra("produtoSelecionado", produto);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemProduto;
        TextView textProdutoNome, textProdutoValor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemProduto = itemView.findViewById(R.id.imagem_produto);
            textProdutoNome = itemView.findViewById(R.id.text_produto_nome);
            textProdutoValor = itemView.findViewById(R.id.text_produto_valor);
        }
    }
}
