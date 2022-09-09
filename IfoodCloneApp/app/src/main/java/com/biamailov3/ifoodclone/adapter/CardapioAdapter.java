package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Categoria;
import com.biamailov3.ifoodclone.model.CategoriaCardapio;

import java.util.List;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.MyViewHolder> {

    private List<CategoriaCardapio> categoriaCardapioList;
    private Context context;

    public CardapioAdapter(List<CategoriaCardapio> categoriaCardapioList, Context context) {
        this.categoriaCardapioList = categoriaCardapioList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cardapio, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoriaCardapio categoriaCardapio = categoriaCardapioList.get(position);

        holder.textCategoriaNome.setText(categoriaCardapio.getNome());

        holder.rvProdutos.setLayoutManager(
                new LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL, false)); // chamando um adapter dentro do outro
        holder.rvProdutos.setHasFixedSize(true);
        ProdutoCardapioAdapter produtoCardapioAdapter = new ProdutoCardapioAdapter(categoriaCardapio.getProdutoList(), context);
        holder.rvProdutos.setAdapter(produtoCardapioAdapter);
        produtoCardapioAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoriaCardapioList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCategoriaNome;
        RecyclerView rvProdutos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textCategoriaNome = itemView.findViewById(R.id.text_categoria_nome);
            rvProdutos = itemView.findViewById(R.id.rv_produtos);
        }
    }
}
