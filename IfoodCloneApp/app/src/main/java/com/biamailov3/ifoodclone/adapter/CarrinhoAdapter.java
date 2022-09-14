package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {

    private List<ItemPedido> itemPedidoList;
    private Context context;
    private OnClickListener onClickListener;

    public CarrinhoAdapter(List<ItemPedido> itemPedidoList, Context context, OnClickListener onClickListener) {
        this.itemPedidoList = itemPedidoList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_carrinho, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemPedido itemPedido = itemPedidoList.get(position);

        holder.textNomeProduto.setText(itemPedido.getItem());
        holder.textTotalProduto.setText(context.getString(R.string.text_valor, GetMask.getValor(itemPedido.getValor() * itemPedido.getQuantidade())));
        Picasso.get().load(itemPedido.getUrlImagem()).into(holder.imagemProduto);
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(itemPedido));
    }

    @Override
    public int getItemCount() {
        return itemPedidoList.size();
    }

    public interface OnClickListener {
        void onClick(ItemPedido itemPedido);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textNomeProduto, textTotalProduto;
        ImageView imagemProduto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomeProduto = itemView.findViewById(R.id.textNomeProduto);
            textTotalProduto = itemView.findViewById(R.id.textTotalProduto);
            imagemProduto = itemView.findViewById(R.id.imagemProduto);
        }
    }
}
