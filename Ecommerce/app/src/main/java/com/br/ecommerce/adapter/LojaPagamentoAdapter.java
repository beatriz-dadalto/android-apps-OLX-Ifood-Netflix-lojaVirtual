package com.br.ecommerce.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Categoria;
import com.br.ecommerce.model.FormaPagamento;
import com.br.ecommerce.model.TipoValor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LojaPagamentoAdapter extends RecyclerView.Adapter<LojaPagamentoAdapter.MyViewHolder> {

    private List<FormaPagamento> formaPagamentoList;
    private OnClick onClick;
    private Context context;

    public LojaPagamentoAdapter(List<FormaPagamento> formaPagamentoList, OnClick onClick, Context context) {
        this.formaPagamentoList = formaPagamentoList;
        this.onClick = onClick;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forma_pagamento_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FormaPagamento formaPagamento = formaPagamentoList.get(position);

        holder.textNomePagamento.setText(formaPagamento.getNome());
        holder.textDescricaoPagamento.setText(formaPagamento.getDescricao());
        holder.textValor.setText(context.getString(R.string.valor, GetMask.getValor(formaPagamento.getValor())));

        if (formaPagamento.getTipoValor().equals(TipoValor.DESCONTO)) {
            holder.textTipoPagamento.setText("Desconto");
        } else if (formaPagamento.getTipoValor().equals(TipoValor.ACRESCIMO)){
            holder.textTipoPagamento.setText("Acréscimo");
        }

        holder.itemView.setOnClickListener(view -> onClick.onClickListener(formaPagamento));
    }

    @Override
    public int getItemCount() {
        return formaPagamentoList.size();
    }

    public interface OnClick {
        void onClickListener(FormaPagamento formaPagamento);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textNomePagamento, textDescricaoPagamento, textValor, textTipoPagamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomePagamento = itemView.findViewById(R.id.textNomePagamento);
            textDescricaoPagamento = itemView.findViewById(R.id.textDescricaoPagamento);
            textValor = itemView.findViewById(R.id.textValor);
            textTipoPagamento = itemView.findViewById(R.id.textTipoPagamento);
        }
    }
}
