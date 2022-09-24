package com.example.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Categoria;
import com.example.ifoodclone.model.Endereco;

import java.util.List;

public class EnderecoAdapter extends RecyclerView.Adapter<EnderecoAdapter.MyViewHolder> {

    private List<Endereco> enderecoList;
    private OnCLickListener onCLickListener;

    public EnderecoAdapter(List<Endereco> enderecoList, OnCLickListener onCLickListener) {
        this.enderecoList = enderecoList;
        this.onCLickListener = onCLickListener;
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

        holder.text_logradouro.setText(endereco.getLogradouro());
        holder.text_referencia.setText(endereco.getReferencia());

        holder.itemView.setOnClickListener(v -> onCLickListener.OnClick(endereco));

    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    public interface OnCLickListener{
        void OnClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text_logradouro, text_referencia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_logradouro = itemView.findViewById(R.id.text_logradouro);
            text_referencia = itemView.findViewById(R.id.text_referencia);
        }
    }

}
