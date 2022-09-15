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
import com.biamailov3.ifoodclone.model.Endereco;

import java.util.List;

public class SelecionaEnderecoAdapter extends RecyclerView.Adapter<SelecionaEnderecoAdapter.MyViewHolder> {

    private final List<Endereco> enderecoList;
    private final OnClickListener onClickListener;

    // listagem comeca do zero entao poe -1 para ele iniciar o radioButton desmarcado
    private int lastSelectedPosition = -1;

    public SelecionaEnderecoAdapter(List<Endereco> enderecoList, OnClickListener onClickListener) {
        this.enderecoList = enderecoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.endereco_select, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Endereco endereco = enderecoList.get(position);

        holder.textLogradouro.setText(endereco.getLogradouro());

        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getReferencia())
                .append(", ")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getMunicipio());

        holder.textEndereco.setText(enderecoCompleto);

        if(lastSelectedPosition == position){
            holder.radioButton.setChecked(true);
        }

        holder.radioButton.setOnClickListener(v -> {
            lastSelectedPosition = position;
            notifyDataSetChanged();
            onClickListener.onClick(endereco);
        });

    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    public interface OnClickListener {
        void onClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView textLogradouro, textEndereco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radioButton);
            textLogradouro = itemView.findViewById(R.id.text_logradouro);
            textEndereco = itemView.findViewById(R.id.text_endereco);
        }
    }
}