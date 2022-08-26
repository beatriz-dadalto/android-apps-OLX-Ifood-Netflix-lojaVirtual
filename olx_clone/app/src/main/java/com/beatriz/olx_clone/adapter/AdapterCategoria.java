package com.beatriz.olx_clone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.model.Categoria;

import java.util.List;

public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {

    private List<Categoria> categoriaList;
    private OnCLickListener onCLickListener;

    public AdapterCategoria(List<Categoria> categoriaList, OnCLickListener onCLickListener) {
        this.categoriaList = categoriaList;
        this.onCLickListener = onCLickListener;
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

        holder.imgCategoria.setImageResource(categoria.getCaminho());
        holder.txtCategoria.setText(categoria.getNome());

        holder.itemView.setOnClickListener(view -> onCLickListener.OnClick(categoria));
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    // quando o user clicar em uma categoria da listagem ser possivel manipular
    public interface OnCLickListener {
        void OnClick(Categoria categoria);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCategoria;
        TextView txtCategoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCategoria = itemView.findViewById(R.id.img_categoria);
            txtCategoria = itemView.findViewById(R.id.txt_categoria);
        }
    }
}
