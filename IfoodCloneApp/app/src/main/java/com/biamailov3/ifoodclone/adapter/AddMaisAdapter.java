package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddMaisAdapter extends RecyclerView.Adapter<AddMaisAdapter.MyViewHolder> {

    private List<Produto> produtoList;
    private List<String> addMaisList;
    private Context context;
    private OnClickListener onClickListener;

    public AddMaisAdapter(List<Produto> produtoList, List<String> addMaisList, Context context, OnClickListener onClickListener) {
        this.produtoList = produtoList;
        this.addMaisList = addMaisList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_mais_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtoList.get(position);

        Picasso.get().load(produto.getUrlImagem()).into(holder.imgProduto);
        holder.textNome.setText(produto.getNome());
        holder.textValor.setText(context.getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        holder.cbStatus.setChecked(addMaisList.contains(produto.getId()));

        holder.cbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onClickListener.onClick(produto.getId(), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClickListener {
        void onClick(String idProduto, Boolean status);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduto;
        TextView textNome, textValor;
        CheckBox cbStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduto = itemView.findViewById(R.id.img_produto);
            textNome = itemView.findViewById(R.id.text_nome);
            textValor = itemView.findViewById(R.id.text_valor);
            cbStatus = itemView.findViewById(R.id.cb_status);
        }
    }

}
