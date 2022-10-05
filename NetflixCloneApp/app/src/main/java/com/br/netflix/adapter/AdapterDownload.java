package com.br.netflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.netflix.R;
import com.br.netflix.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDownload extends RecyclerView.Adapter<AdapterDownload.MyViewHolder> {

    private List<Post> postList;
    private final OnClickListener clickListener;

    public AdapterDownload(List<Post> postList, OnClickListener clickListener) {
        this.postList = postList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_download, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = postList.get(position);

        Picasso.get().load(post.getImagem()).into(holder.imagem);
        holder.textTitulo.setText(post.getTitulo());
        holder.checkBox.setChecked(true);

        holder.checkBox.setOnClickListener(view -> clickListener.onItemClickListener(post));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnClickListener {
        void onItemClickListener(Post post);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagem;
        TextView textTitulo;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imagem);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}
