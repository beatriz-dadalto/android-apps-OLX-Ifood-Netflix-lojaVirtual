package com.br.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.model.Categoria;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoriaDialogAdapter extends RecyclerView.Adapter<CategoriaDialogAdapter.MyViewHolder> {

    private List<String> idsCategoriasSelecionadas;
    private List<Categoria> categoriaList;
    private Context context;
    private OnClick onClick;

    public CategoriaDialogAdapter(List<String> idsCategoriasSelecionadas, List<Categoria> categoriaList, Context context, OnClick onClick) {
        this.idsCategoriasSelecionadas = idsCategoriasSelecionadas;
        this.categoriaList = categoriaList;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria_dialog, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);

        Glide
                .with(context)
                .load(categoria.getUrlImagem())
                .centerCrop()
                .into(holder.imagemCategoria);

        holder.nomeCategoria.setText(categoria.getNome());

        if (idsCategoriasSelecionadas.contains(categoria.getId())) {
            holder.checkBox.setChecked(true);
        }

        holder.itemView.setOnClickListener(view -> {
            onClick.onClickListener(categoria);
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
        });
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
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeCategoria = itemView.findViewById(R.id.nomeCategoria);
            imagemCategoria = itemView.findViewById(R.id.imagemCategoria);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
