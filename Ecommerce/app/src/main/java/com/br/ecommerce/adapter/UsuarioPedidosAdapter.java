package com.br.ecommerce.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.R;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Pedido;
import com.br.ecommerce.model.PedidoStatus;

import java.util.List;

public class UsuarioPedidosAdapter extends RecyclerView.Adapter<UsuarioPedidosAdapter.MyViewHolder> {

    private List<Pedido> pedidoList;
    private Context context;
    private OnClickListener clickListener;

    public UsuarioPedidosAdapter(List<Pedido> pedidoList, Context context, OnClickListener clickListener) {
        this.pedidoList = pedidoList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_pedido_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pedido pedido = pedidoList.get(position);

        holder.textIdPedido.setText(pedido.getId());
        holder.textTotalPedido.setText(context.getString(R.string.valor, GetMask.getValor(pedido.getTotal())));
        holder.textDataPedido.setText(GetMask.getDate(pedido.getDataPedido(), 2));

        switch (pedido.getStatusPedido()) {
            case PENDENTE:
                holder.textStatusPedido.setTextColor(Color.parseColor("#FC6E20"));
                break;
            case APROVADO:
                holder.textStatusPedido.setTextColor(Color.parseColor("#34A853"));
                break;
            default:
                holder.textStatusPedido.setTextColor(Color.parseColor("#E94235"));
                break;
        }

        holder.btnDetalhesPedido.setOnClickListener(view -> clickListener.onClick(pedido));
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    public interface OnClickListener {
        void onClick(Pedido pedido);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textIdPedido, textStatusPedido, textTotalPedido, textDataPedido;
        Button btnDetalhesPedido;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textIdPedido = itemView.findViewById(R.id.textIdPedido);
            textStatusPedido = itemView.findViewById(R.id.textStatusPedido);
            textTotalPedido = itemView.findViewById(R.id.textTotalPedido);
            textDataPedido = itemView.findViewById(R.id.textDataPedido);
            btnDetalhesPedido = itemView.findViewById(R.id.btnDetalhesPedido);
        }
    }
}
