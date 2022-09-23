package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.ItemPedido;

import java.util.List;

public class ItemDetalhePedidoAdapter extends RecyclerView.Adapter<ItemDetalhePedidoAdapter.MyViewHolder> {

    private final List<ItemPedido> itemPedidoList;
    private Context context;

    public ItemDetalhePedidoAdapter(List<ItemPedido> itemPedidoList, Context context) {
        this.itemPedidoList = itemPedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_detalhe_pedido, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemPedido itemPedido = itemPedidoList.get(position);

        holder.textQtdItemPedido.setText(String.valueOf(itemPedido.getQuantidade()));
        holder.textNomeItemPedido.setText(itemPedido.getItem());
        holder.textValorItem.setText(context.getString(R.string.text_valor,
                GetMask.getValor(itemPedido.getValor())));
    }

    @Override
    public int getItemCount() {
        return itemPedidoList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textQtdItemPedido, textNomeItemPedido, textValorItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textQtdItemPedido = itemView.findViewById(R.id.text_qtd_item_pedido);
            textNomeItemPedido = itemView.findViewById(R.id.text_nome_item_pedido);
            textValorItem = itemView.findViewById(R.id.text_valor_item);
        }
    }
}
