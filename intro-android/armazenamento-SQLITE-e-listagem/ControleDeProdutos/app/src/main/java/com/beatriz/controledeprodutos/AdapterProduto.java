package com.beatriz.controledeprodutos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

    private List<Produto> produtoList;
    private OnClick onClick;

    public AdapterProduto(List<Produto> produtoList, OnClick onClick) {
        this.produtoList = produtoList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new MyViewHolder(itemView);
    }

    // show on screen
    // roda para cada produto
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = produtoList.get(position);

        holder.textProduto.setText(produto.getNome());
        holder.textEstoque.setText("Estoque: " + produto.getEstoque());
        holder.textValor.setText("R$ " + produto.getValor());

        holder.itemView.setOnClickListener(view -> onClick.onClickListener(produto));
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClick {
        void onClickListener(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textProduto, textEstoque, textValor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProduto = itemView.findViewById(R.id.text_produto);
            textEstoque = itemView.findViewById(R.id.text_estoque);
            textValor = itemView.findViewById(R.id.text_valor);
        }
    }

}
