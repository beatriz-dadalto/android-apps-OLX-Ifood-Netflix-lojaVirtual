package com.br.ecommerce.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.FormaPagamento;
import com.br.ecommerce.model.TipoValor;

import java.util.List;

public class UsuarioPagamentoAdapter extends RecyclerView.Adapter<UsuarioPagamentoAdapter.MyViewHolder> {

    private List<FormaPagamento> formaPagamentoList;
    private OnClick onClick;
    private int row_index = -1;

    public UsuarioPagamentoAdapter(List<FormaPagamento> formaPagamentoList, OnClick onClick) {
        this.formaPagamentoList = formaPagamentoList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagamento_pedido_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FormaPagamento formaPagamento = formaPagamentoList.get(position);

        holder.textNomePagamento.setText(formaPagamento.getNome());
        holder.textDescricaoPagamento.setText(formaPagamento.getDescricao());


        holder.itemView.setOnClickListener(view -> {

            onClick.onClickListener(formaPagamento);

            row_index = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (row_index == holder.getAdapterPosition()) {
            holder.rbCheck.setChecked(true);
        } else {
            holder.rbCheck.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return formaPagamentoList.size();
    }

    public interface OnClick {
        void onClickListener(FormaPagamento formaPagamento);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton rbCheck;
        TextView textNomePagamento, textDescricaoPagamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomePagamento = itemView.findViewById(R.id.textNomePagamento);
            textDescricaoPagamento = itemView.findViewById(R.id.textDescricaoPagamento);
            rbCheck = itemView.findViewById(R.id.rbCheck);
        }
    }
}
