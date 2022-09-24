package com.example.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Pagamento;

import java.util.List;

public class SelecionaPagamentoAdapter extends RecyclerView.Adapter<SelecionaPagamentoAdapter.MyViewHolder> {

    private final List<Pagamento> pagamentoList;
    private final OnClickListener onClickListener;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pagamento pagamento = pagamentoList.get(position);

        holder.text_forma_pagamento.setText(pagamento.getDescricao());

        if(lastSelectedPosition == position){
            holder.radioButton.setChecked(true);
        }

        holder.radioButton.setOnClickListener(v -> {
            lastSelectedPosition = position;
            notifyDataSetChanged();
            onClickListener.OnClick(pagamento);
        });

    }

    @Override
    public int getItemCount() {
        return pagamentoList.size();
    }

    public interface OnClickListener {
        void OnClick(Pagamento pagamento);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView text_forma_pagamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radioButton);
            text_forma_pagamento = itemView.findViewById(R.id.text_forma_pagamento);
        }
    }
}
