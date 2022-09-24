package com.example.ifoodclone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProdutoCardapioAdapter extends RecyclerView.Adapter<ProdutoCardapioAdapter.MyViewHolder> {

    private final List<Produto> produtoList;
    private final Activity activity;

    public ProdutoCardapioAdapter(List<Produto> produtoList, Activity activity) {
        this.produtoList = produtoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto_cardapio, parent, false);
        return new MyViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = produtoList.get(position);

        Picasso.get().load(produto.getUrlImagem()).into(holder.imagemProduto);
        holder.textProdutoNome.setText(produto.getNome());
        holder.textProdutoValor.setText(activity.getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, EmpresaProdutoDetalhesActivity.class);
            intent.putExtra("produtoSelecionado", produto);
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagemProduto;
        TextView textProdutoNome, textProdutoValor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemProduto = itemView.findViewById(R.id.imagemProduto);
            textProdutoNome = itemView.findViewById(R.id.textProdutoNome);
            textProdutoValor = itemView.findViewById(R.id.textProdutoValor);
        }
    }
}
