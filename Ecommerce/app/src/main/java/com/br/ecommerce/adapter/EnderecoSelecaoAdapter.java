package com.br.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.model.Endereco;

import java.util.List;

public class EnderecoSelecaoAdapter extends RecyclerView.Adapter<EnderecoSelecaoAdapter.MyViewHolder> {

    private List<Endereco> enderecoList;
    private OnClickListener clickListener;

    public EnderecoSelecaoAdapter(List<Endereco> enderecoList, OnClickListener clickListener) {
        this.enderecoList = enderecoList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selecao_endereco_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Endereco endereco = enderecoList.get(position);

        holder.textNomeEndereco.setText(endereco.getNomeEndereco());

        StringBuilder enderecoCompleto = new StringBuilder();
        enderecoCompleto.append(endereco.getLogradouro())
                .append(", ")
                .append(endereco.getNumero())
                .append(endereco.getBairro())
                .append(endereco.getLocalidade())
                .append(endereco.getUf())
                .append("CEP: ")
                .append(endereco.getCep());

        holder.textEndereco.setText(enderecoCompleto);

        holder.itemView.setOnClickListener(view -> clickListener.onClick(endereco));

    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    public interface OnClickListener {
        public void onClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton rbCheck;
        TextView textNomeEndereco;
        TextView textEndereco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomeEndereco = itemView.findViewById(R.id.textNomeEndereco);
            textEndereco = itemView.findViewById(R.id.textLogradouro);

        }
    }
}
