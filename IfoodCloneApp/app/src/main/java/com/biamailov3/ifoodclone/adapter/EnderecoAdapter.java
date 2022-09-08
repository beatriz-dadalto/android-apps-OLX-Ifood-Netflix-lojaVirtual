package com.biamailov3.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Categoria;
import com.biamailov3.ifoodclone.model.Endereco;

import java.util.List;

public class EnderecoAdapter extends RecyclerView.Adapter<EnderecoAdapter.MyViewHolder> {

    private List<Endereco> enderecoList;
    private OnClickListener onClickListener;

    public EnderecoAdapter(List<Endereco> enderecoList, OnClickListener onClickListener) {
        this.enderecoList = enderecoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.endereco_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Endereco endereco = enderecoList.get(position);

        holder.textLogradouro.setText(endereco.getLogradouro());
        holder.textReferencia.setText(endereco.getReferencia());

        holder.itemView.setOnClickListener(view -> onClickListener.onClick(endereco));
    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    public interface OnClickListener {
        void onClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textLogradouro;
        TextView textReferencia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textLogradouro = itemView.findViewById(R.id.text_logradouro);
            textReferencia = itemView.findViewById(R.id.text_referencia);
        }
    }
}
