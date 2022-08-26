package com.beatriz.casaportemporada.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.model.Anuncio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {

    private List<Anuncio> anuncioList;
    private OnClick onClick;

    public AdapterAnuncios(List<Anuncio> anuncioList, OnClick onClick) {
        this.anuncioList = anuncioList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Anuncio anuncio = anuncioList.get(position);

        Picasso.get().load(anuncio.getUrlImagem()).into(holder.imgAnuncio);
        holder.textTitulo.setText(anuncio.getTitulo());
        holder.textDescricao.setText(anuncio.getDescricao());
        holder.textData.setText("");

        holder.itemView.setOnClickListener(v -> onClick.onClickListener(anuncio));
    }

    @Override
    public int getItemCount() {
        return anuncioList.size();
    }

    public interface OnClick {
        public void onClickListener(Anuncio anuncio);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAnuncio;
        TextView textTitulo, textDescricao, textData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnuncio = itemView.findViewById(R.id.img_anuncio);
            textTitulo = itemView.findViewById(R.id.text_titulo_anuncio);
            textDescricao = itemView.findViewById(R.id.text_descricao);
            textData = itemView.findViewById(R.id.text_data);
        }
    }
}
