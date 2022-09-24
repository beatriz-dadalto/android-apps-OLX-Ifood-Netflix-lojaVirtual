package com.example.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.ItemPedido;

import java.util.List;

public class ItemDetalhePedidoAdapter extends RecyclerView.Adapter<ItemDetalhePedidoAdapter.MyViewHolder> {

    private final List<ItemPedido> itemPedidoList;
    private final Context context;

    public ItemDetalhePedidoAdapter(List<ItemPedido> itemPedidoList, Context context) {
        this.itemPedidoList = itemPedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_detalhe_pedido, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemPedido itemPedido = itemPedidoList.get(position);

        holder.text_qtd_item_pedido.setText(String.valueOf(itemPedido.getQuantidade()));
        holder.text_nome_item_pedido.setText(itemPedido.getItem());
        holder.text_valor_item.setText(context.getString(R.string.text_valor, GetMask.getValor(itemPedido.getValor())));

    }

    @Override
    public int getItemCount() {
        return itemPedidoList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text_qtd_item_pedido, text_nome_item_pedido, text_valor_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text_qtd_item_pedido = itemView.findViewById(R.id.text_qtd_item_pedido);
            text_nome_item_pedido = itemView.findViewById(R.id.text_nome_item_pedido);
            text_valor_item = itemView.findViewById(R.id.text_valor_item);
        }
    }
}
