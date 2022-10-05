package com.br.netflix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.netflix.R;
import com.br.netflix.activity.DetalheActivity;
import com.br.netflix.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    private List<Post> postList;
    private final Context context;

    public AdapterPost(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = postList.get(position);
        Picasso.get().load(post.getImagem()).into(holder.imagem);


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetalheActivity.class);
            intent.putExtra("postSelecionado", post);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imagem);
        }
    }
}
