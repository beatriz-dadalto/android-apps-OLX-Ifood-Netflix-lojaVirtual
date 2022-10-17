package com.br.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LojaProdutoAdapter extends RecyclerView.Adapter<LojaProdutoAdapter.MyViewHolder> {

    private List<Produto> produtoList;
    private Context context;
    private OnClickLister onClickLister;

    public LojaProdutoAdapter(List<Produto> produtoList, Context context, OnClickLister onClickLister) {
        this.produtoList = produtoList;
        this.context = context;
        this.onClickLister = onClickLister;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtoList.get(position);

        holder.txtNomeProduto.setText(produto.getTitulo());
        
        for (int i = 0; i < produto.getUrlsImagens().size(); i++) {
            if (produto.getUrlsImagens().get(i).getIndex() == 0) {
                Picasso.get()
                        .load(produto.getUrlsImagens().get(i).getCaminhoImagem())
                        .into(holder.imagemProduto);
            }
        }

        holder.txtValorProduto.setText(String.valueOf(produto.getValorAtual()));
        holder.txtDescontoProduto.setText("15% OFF");
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    private interface OnClickLister {
        void onClick(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemProduto;
        TextView txtNomeProduto;
        TextView txtValorProduto;
        TextView txtDescontoProduto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemProduto = itemView.findViewById(R.id.imagemProduto);
            txtNomeProduto = itemView.findViewById(R.id.txtNomeProduto);
            txtValorProduto = itemView.findViewById(R.id.txtValorProduto);
            txtDescontoProduto = itemView.findViewById(R.id.txtDescontoProduto);
        }
    }
}
