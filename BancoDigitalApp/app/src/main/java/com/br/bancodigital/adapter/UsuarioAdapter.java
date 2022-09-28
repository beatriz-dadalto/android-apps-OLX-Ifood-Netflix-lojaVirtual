package com.br.bancodigital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.bancodigital.R;
import com.br.bancodigital.model.Usuario;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.MyViewHolder> {

    private List<Usuario> usuarioList;

    public UsuarioAdapter(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usuario, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = usuarioList.get(position);

        holder.textUsuario.setText(usuario.getNome());

        if (usuario.getUrlImagem() != null) {
            Picasso.get().load(usuario.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .into(holder.imagemUsuario);
        } else {
            holder.imagemUsuario.setImageResource(R.drawable.ic_user);
        }

    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    static  class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemUsuario;
        TextView textUsuario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemUsuario = itemView.findViewById(R.id.imagemUsuario);
            textUsuario = itemView.findViewById(R.id.textUsuario);
        }
    }
}
