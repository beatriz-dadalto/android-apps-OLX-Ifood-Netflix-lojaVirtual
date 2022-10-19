package com.br.ecommerce.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.model.Categoria;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {

    private int layout;
    private boolean background;
    private int row_index = 0;
    private List<Categoria> categoriaList;
    private OnClick onClick;

    public CategoriaAdapter(int layout, boolean background, List<Categoria> categoriaList, OnClick onClick) {
        this.layout = layout;
        this.background = background;
        this.categoriaList = categoriaList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);

        if (background) {

            holder.itemView.setOnClickListener(view -> {
                onClick.onClickListener(categoria);

                row_index = holder.getAdapterPosition();
                notifyDataSetChanged();
            });

            if (row_index == holder.getAdapterPosition()) {
                holder.itemView.setBackgroundResource(R.drawable.bg_categoria_home);
                holder.nomeCategoria.setTextColor(Color.parseColor("#FFFFFF"));
                holder.imagemCategoria.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.nomeCategoria.setTextColor(Color.parseColor("#808080"));
                holder.imagemCategoria.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.itemView.setOnClickListener(v -> onClick.onClickListener(categoria));
        }

        holder.nomeCategoria.setText(categoria.getNome());
        Picasso.get().load(categoria.getUrlImagem()).into(holder.imagemCategoria);
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public interface OnClick {
        void onClickListener(Categoria categoria);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCategoria;
        ImageView imagemCategoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeCategoria = itemView.findViewById(R.id.nomeCategoria);
            imagemCategoria = itemView.findViewById(R.id.imagemCategoria);
        }
    }
}
