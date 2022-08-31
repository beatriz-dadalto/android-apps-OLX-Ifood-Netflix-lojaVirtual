package com.biamailov3.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {

    private List<Categoria> categoriaList;
    private OnClickListener onClickListener;

    public CategoriaAdapter(List<Categoria> categoriaList, OnClickListener onClickListener) {
        this.categoriaList = categoriaList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);

        holder.textCategoria.setText(categoria.getNome());
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(categoria, position));
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public interface OnClickListener {
        void onClick(Categoria categoria, int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCategoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoria = itemView.findViewById(R.id.text_categoria);
        }
    }
}
