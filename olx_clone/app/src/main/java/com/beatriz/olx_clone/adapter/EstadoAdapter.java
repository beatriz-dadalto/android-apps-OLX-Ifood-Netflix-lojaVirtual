package com.beatriz.olx_clone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.model.Estado;

import java.util.List;

public class EstadoAdapter extends RecyclerView.Adapter<EstadoAdapter.MyViewHolder> {

    private List<Estado> estadoList;
    private OnClickListener onClickListener;

    public EstadoAdapter(List<Estado> estadoList, OnClickListener onClickListener) {
        this.estadoList = estadoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.estado_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Estado estado = estadoList.get(position);

        holder.textEstado.setText(estado.getNome());
        holder.itemView.setOnClickListener(view -> onClickListener.OnClick(estado));
    }

    @Override
    public int getItemCount() {
        return estadoList.size();
    }

    public interface OnClickListener {
        void OnClick(Estado estado);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textEstado;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textEstado = itemView.findViewById(R.id.text_estado);
        }
    }
}
