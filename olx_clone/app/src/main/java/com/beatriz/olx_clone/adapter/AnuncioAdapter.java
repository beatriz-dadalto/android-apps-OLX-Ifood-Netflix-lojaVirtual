package com.beatriz.olx_clone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.GetMask;
import com.beatriz.olx_clone.model.Anuncio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.MyViewHolder> {

    private List<Anuncio> anuncioList;
    private OnClickListener onClickListener;

    public AnuncioAdapter(List<Anuncio> anuncioList, OnClickListener onClickListener) {
        this.anuncioList = anuncioList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anuncio_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Anuncio anuncio = anuncioList.get(position);

        for (int i = 0; i < anuncio.getUrlImagens().size(); i++) {
            if (anuncio.getUrlImagens().get(i).getIndex() == 0) {
                Picasso.get().load(anuncio.getUrlImagens().get(i).getCaminhoImagem()).into(holder.imgAnuncio);
            }
        }

        holder.textTitulo.setText(anuncio.getTitulo());
        holder.textValor.setText("R$ " + GetMask.getValor(anuncio.getValor()));
        holder.textLocal.setText(GetMask.getDate(anuncio.getDataPublicacao(), 4) + ", " + anuncio.getLocal().getBairro());

        holder.itemView.setOnClickListener(v -> onClickListener.onClick(anuncio));
    }

    @Override
    public int getItemCount() {
        return anuncioList.size();
    }

    // gerenciar cliques nos anuncios
    public interface OnClickListener {
        void onClick(Anuncio anuncio);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAnuncio;
        TextView textTitulo;
        TextView textValor;
        TextView textLocal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnuncio = itemView.findViewById(R.id.img_anuncio);
            textTitulo = itemView.findViewById(R.id.text_titulo);
            textValor = itemView.findViewById(R.id.text_valor);
            textLocal = itemView.findViewById(R.id.text_local);
        }
    }
}
