package com.br.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.model.Endereco;

import java.util.List;

public class EnderecoAdapter extends RecyclerView.Adapter<EnderecoAdapter.MyViewHolder> {

    private List<Endereco> enderecoList;
    private OnClickListener clickListener;

    public EnderecoAdapter(List<Endereco> enderecoList, OnClickListener clickListener) {
        this.enderecoList = enderecoList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Endereco endereco = enderecoList.get(position);

        holder.textNomeEndereco.setText(endereco.getNomeEndereco());
        holder.textLogradouro.setText(endereco.getLogradouro());

        if (!endereco.getNumero().isEmpty()) {
            holder.textNumEndereco.setText(endereco.getNumero());
        }

        holder.itemView.setOnClickListener(view -> clickListener.onClick(endereco));

    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    interface OnClickListener {
        public void onClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textNomeEndereco;
        TextView textLogradouro;
        TextView textNumEndereco;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomeEndereco = itemView.findViewById(R.id.textNomeEndereco);
            textLogradouro = itemView.findViewById(R.id.textLogradouro);
            textNumEndereco = itemView.findViewById(R.id.textNumEndereco);
        }
    }
}
