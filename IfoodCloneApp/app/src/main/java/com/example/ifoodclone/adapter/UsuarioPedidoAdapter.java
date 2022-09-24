package com.example.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Pedido;
import com.example.ifoodclone.model.StatusPedido;
import com.example.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsuarioPedidoAdapter extends RecyclerView.Adapter<UsuarioPedidoAdapter.MyViewHolder> {

    private final List<Pedido> pedidoList;
    private final Context context;
    private final OnClickListener onClickListener;

    public UsuarioPedidoAdapter(List<Pedido> pedidoList, Context context, OnClickListener onClickListener) {
        this.pedidoList = pedidoList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedido_usuario, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pedido pedido = pedidoList.get(position);

        if(pedido.getStatusPedido() == StatusPedido.CANCELADO_USUARIO ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_EMPRESA ||
        pedido.getStatusPedido() == StatusPedido.ENTREGUE){
            holder.btn_ajuda.setEnabled(false);
        }

        if(pedido.getDataPedido() != null){
            holder.text_data_pedido.setText(GetMask.getDate(pedido.getDataPedido(), 3));
        }

        holder.text_status_pedido.setText(StatusPedido.getStatus(pedido.getStatusPedido()));

        holder.text_qtd_item_pedido.setText(String.valueOf(pedido.getItemPedidoList().get(0).getQuantidade()));
        holder.text_nome_item_pedido.setText(pedido.getItemPedidoList().get(0).getItem());

        if(pedido.getItemPedidoList().size() > 1){
            holder.text_mais_itens.setText(
                    context.getString(R.string.mais_item, pedido.getItemPedidoList().size() - 1));
            holder.text_mais_itens.setVisibility(View.VISIBLE);
        }else {
            holder.text_mais_itens.setVisibility(View.GONE);
        }

        recuperaEmpresa(holder, pedido.getIdEmpresa());


        holder.btn_ajuda.setOnClickListener(v -> onClickListener.OnClick(pedido, 0));
        holder.btn_detalhes.setOnClickListener(v -> onClickListener.OnClick(pedido, 1));

    }

    private void recuperaEmpresa(MyViewHolder holder, String idEmpresa){
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(idEmpresa);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empresa empresa = snapshot.getValue(Empresa.class);
                if(empresa != null){
                    holder.text_empresa.setText(empresa.getNome());
                    Picasso.get().load(empresa.getUrlLogo()).into(holder.img_log_empresa);
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

    public interface OnClickListener{
        void OnClick(Pedido pedido, int rota);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_log_empresa;
        TextView text_empresa, text_status_pedido, text_data_pedido, text_qtd_item_pedido,
        text_nome_item_pedido, text_mais_itens;

        Button btn_ajuda, btn_detalhes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_log_empresa = itemView.findViewById(R.id.img_log_empresa);
            text_empresa = itemView.findViewById(R.id.text_empresa);
            text_status_pedido = itemView.findViewById(R.id.text_status_pedido);
            text_data_pedido = itemView.findViewById(R.id.text_data_pedido);
            text_qtd_item_pedido = itemView.findViewById(R.id.text_qtd_item_pedido);
            text_nome_item_pedido = itemView.findViewById(R.id.text_nome_item_pedido);
            text_mais_itens = itemView.findViewById(R.id.text_mais_itens);
            btn_ajuda = itemView.findViewById(R.id.btn_ajuda);
            btn_detalhes = itemView.findViewById(R.id.btn_detalhes);
        }
    }
}
