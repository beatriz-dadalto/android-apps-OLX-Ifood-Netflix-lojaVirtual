package com.br.netflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.netflix.R;
import com.br.netflix.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBreve extends RecyclerView.Adapter<AdapterBreve.MyViewHolder> {

    private List<Post> postList;

    public AdapterBreve(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_em_breve, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = postList.get(position);

        Picasso.get().load(post.getImagem()).into(holder.imagem);
        holder.textTitulo.setText(post.getTitulo());
        holder.textDescricao.setText(post.getSinopse());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagem;
        TextView textTitulo, textDescricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imagem);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDescricao = itemView.findViewById(R.id.textDescricao);
        }
    }

}
