package com.br.netflix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.netflix.R;
import com.br.netflix.model.Categoria;

import java.util.List;

public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {

    private List<Categoria> categoriaList;
    private Context context;

    public AdapterCategoria(List<Categoria> categoriaList, Context context) {
        this.categoriaList = categoriaList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Categoria categoria = categoriaList.get(position);

        holder.textCategoria.setText(categoria.getNome());

        carregaFilmes(categoria, holder);

    }

    private void carregaFilmes(Categoria categoria, MyViewHolder holder) {
        holder.rvListagem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvListagem.setHasFixedSize(true);

        AdapterPost adapterPost = new AdapterPost(categoria.getPostList());

        holder.rvListagem.setAdapter(adapterPost);

        adapterPost.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCategoria;
        RecyclerView rvListagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textCategoria = itemView.findViewById(R.id.textCategoria);
            rvListagem = itemView.findViewById(R.id.rvListagem);
        }
    }

}
