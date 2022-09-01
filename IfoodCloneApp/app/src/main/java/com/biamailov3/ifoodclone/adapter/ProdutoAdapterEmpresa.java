package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapterEmpresa extends RecyclerView.Adapter<ProdutoAdapterEmpresa.MyViewHolder> {

    private List<Produto> produtoList;
    private Context context;
    private OnClickListener onClickListener;

    public ProdutoAdapterEmpresa(List<Produto> produtoList, Context context, OnClickListener onClickListener) {
        this.produtoList = produtoList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtoList.get(position);

        Picasso.get().load(produto.getUrlImagem()).into(holder.imgProduto);
        holder.textNome.setText(produto.getNome());
        holder.textDescricao.setText(produto.getDescricao());
        holder.textValor.setText(context.getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        if (produto.getValorAntigo() > 0) {
            holder.textvalorAntigo.setText(context.getString(R.string.text_valor, GetMask.getValor(produto.getValorAntigo())));
        } else {
            holder.textvalorAntigo.setText("");
        }

        holder.itemView.setOnClickListener(view -> onClickListener.onClick(produto));
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClickListener {
        void onClick(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduto;
        TextView textNome, textDescricao, textValor, textvalorAntigo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduto = itemView.findViewById(R.id.img_produto);
            textNome = itemView.findViewById(R.id.text_nome);
            textDescricao = itemView.findViewById(R.id.text_descricao);
            textValor = itemView.findViewById(R.id.text_valor);
            textvalorAntigo = itemView.findViewById(R.id.text_valor_antigo);
        }
    }
}
