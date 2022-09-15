package com.biamailov3.ifoodclone.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Pagamento;

import java.util.List;

public class SelecionaPagamentoAdapter extends RecyclerView.Adapter<SelecionaPagamentoAdapter.MyViewHolder> {

    private final List<Pagamento> pagamentoList;
    private final OnClickListener onClickListener;

    // listagem comeca do zero entao poe -1 para ele iniciar o radioButton desmarcado
    private int lastSelectedPosition = -1;

    public SelecionaPagamentoAdapter(List<Pagamento> pagamentoList, OnClickListener onClickListener) {
        this.pagamentoList = pagamentoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagamento_select, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Pagamento pagamento = pagamentoList.get(position);

        holder.textFormaPagamento.setText(pagamento.getDescricao());

        if(lastSelectedPosition == position){
            holder.radioButton.setChecked(true);
        }

        holder.radioButton.setOnClickListener(v -> {
            lastSelectedPosition = position;
            notifyDataSetChanged();
            onClickListener.onClick(pagamento);
        });

    }

    @Override
    public int getItemCount() {
        return pagamentoList.size();
    }

    public interface OnClickListener {
        void onClick(Pagamento pagamento);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView textFormaPagamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radioButton);
            textFormaPagamento = itemView.findViewById(R.id.text_forma_pagamento);
        }
    }
}