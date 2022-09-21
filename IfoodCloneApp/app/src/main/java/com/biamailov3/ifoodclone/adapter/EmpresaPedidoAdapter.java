package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.StatusPedido;
import com.biamailov3.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EmpresaPedidoAdapter extends RecyclerView.Adapter<EmpresaPedidoAdapter.MyViewHolder> {

    private final List<Pedido> pedidoList;
    private final Context context;
    private OnClickListener onClickListener;

    public EmpresaPedidoAdapter(List<Pedido> pedidoList, Context context, OnClickListener onClickListener) {
        this.pedidoList = pedidoList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedido_empresa, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pedido pedido = pedidoList.get(position);

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO_USUARIO ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_EMPRESA ||
                pedido.getStatusPedido() == StatusPedido.ENTREGUE) {
            holder.btnStatus.setEnabled(false);
        }

        if (pedido.getDataPedido() != null) {
            holder.textDataPedido.setText(GetMask.getDate(pedido.getDataPedido(), 3));
        }

        holder.textStatusPedido.setText(StatusPedido.getStatus(pedido.getStatusPedido()));
        holder.textQtdItemPedido.setText(String.valueOf(pedido.getItemPedidoList().get(0).getQuantidade()));
        holder.textNomeItemPedido.setText(pedido.getItemPedidoList().get(0).getItem());

        if (pedido.getItemPedidoList().size() > 1) {
            holder.textMaisitens.setText(context.getString(R.string.mais_itens, pedido.getItemPedidoList().size() - 1));
        } else {
            holder.textMaisitens.setVisibility(View.GONE);
        }

        recuperarCliente(holder, pedido.getIdCliente());

        holder.btnStatus.setOnClickListener(view -> onClickListener.onClick(pedido, 0));
        holder.btnDetalhes.setOnClickListener(view -> onClickListener.onClick(pedido, 1));
    }

    private void recuperarCliente(MyViewHolder holder, String idCliente) {
        DatabaseReference clienteRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(idCliente);
        clienteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                if (usuario != null) {
                    holder.textCliente.setText(usuario.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    // rota eh para saber se o user clicou no botao status ou detalhes
    public interface OnClickListener {
        void onClick(Pedido pedido, int rota);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCliente, textStatusPedido, textDataPedido, textQtdItemPedido,
        textNomeItemPedido, textMaisitens;
        Button btnStatus, btnDetalhes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textCliente = itemView.findViewById(R.id.text_cliente);
            textStatusPedido = itemView.findViewById(R.id.text_status_pedido);
            textDataPedido = itemView.findViewById(R.id.text_data_pedido);
            textQtdItemPedido = itemView.findViewById(R.id.text_qtd_item_pedido);
            textNomeItemPedido = itemView.findViewById(R.id.text_nome_item_pedido);
            textMaisitens = itemView.findViewById(R.id.text_mais_itens);
            btnStatus = itemView.findViewById(R.id.btn_status);
            btnDetalhes = itemView.findViewById(R.id.btn_detalhes);
        }
    }
}
