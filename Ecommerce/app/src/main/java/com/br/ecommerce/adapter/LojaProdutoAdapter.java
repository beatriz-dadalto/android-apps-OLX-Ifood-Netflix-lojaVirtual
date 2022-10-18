package com.br.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
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

        if (produto.getValorAntigo() > 0) {
            double resto = produto.getValorAntigo() - produto.getValorAtual();
            int porcentagem = (int) (resto / produto.getValorAntigo() * 100);

            if (porcentagem >= 10) {
                holder.txtDescontoProduto.setText(context.getString(R.string.valor_off, porcentagem, "%"));
            } else {
                String porcent = String.valueOf(porcentagem).replace("0", "");
                holder.txtDescontoProduto.setText(context.getString(R.string.valor_off, Integer.parseInt(porcent), "%"));
            }
        } else {
            holder.txtDescontoProduto.setText(View.GONE);
        }

        for (int i = 0; i < produto.getUrlsImagens().size(); i++) {
            if (produto.getUrlsImagens().get(i).getIndex() == 0) {
                Picasso.get()
                        .load(produto.getUrlsImagens().get(i).getCaminhoImagem())
                        .into(holder.imagemProduto);
            }
        }

        holder.txtValorProduto.setText(String.valueOf(produto.getValorAtual()));

        holder.itemView.setOnClickListener(v -> onClickLister.onClick(produto));
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClickLister {
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
