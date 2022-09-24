package com.example.ifoodclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.empresa.EmpresaProdutoDetalhesActivity;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProdutoCarrinhoAdapter extends RecyclerView.Adapter<ProdutoCarrinhoAdapter.MyViewHolder> {

    private final List<Produto> produtoList;
    private final Context context;
    private final OnClickListener onClickListener;

    public ProdutoCarrinhoAdapter(List<Produto> produtoList, Context context, OnClickListener onClickListener) {
        this.produtoList = produtoList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_tambem, parent, false);
        return new MyViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = produtoList.get(position);

        Picasso.get().load(produto.getUrlImagem()).into(holder.imagemProduto);
        holder.textNomeProduto.setText(produto.getNome());
        holder.textPrecoProduto.setText(context.getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        holder.ibAdd.setOnClickListener(v -> onClickListener.OnClick(produto));

    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClickListener {
        void OnClick(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagemProduto;
        TextView textNomeProduto, textPrecoProduto;
        ImageButton ibAdd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemProduto = itemView.findViewById(R.id.imagemProduto);
            textNomeProduto = itemView.findViewById(R.id.textNomeProduto);
            textPrecoProduto = itemView.findViewById(R.id.textPrecoProduto);
            ibAdd = itemView.findViewById(R.id.ibAdd);
        }
    }
}
