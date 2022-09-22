package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.StatusPedido;
import com.biamailov3.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsuarioPedidoAdapter extends RecyclerView.Adapter<UsuarioPedidoAdapter.MyViewHolder> {

    private final List<Pedido> pedidoList;
    private final Context context;
    private OnClickListener onClickListener;

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

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO_USUARIO ||
        pedido.getStatusPedido() == StatusPedido.CANCELADO_EMPRESA ||
                pedido.getStatusPedido() == StatusPedido.ENTREGUE) {
            holder.btnAjuda.setEnabled(false);
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

        recuperarEmpresa(holder, pedido.getIdEmpresa());

        holder.btnAjuda.setOnClickListener(view -> onClickListener.onClick(pedido, 0));
        holder.btnDetalhes.setOnClickListener(view -> onClickListener.onClick(pedido, 1));
    }

    private void recuperarEmpresa(MyViewHolder holder, String idEmpresa) {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(idEmpresa);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empresa empresa = snapshot.getValue(Empresa.class);
                if (empresa != null) {
                    holder.textEmpresa.setText(empresa.getNome());
                    Picasso.get().load(empresa.getUrlLogo()).into(holder.imgLogoEmpresa);
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

    public interface OnClickListener {
        void onClick(Pedido pedido, int rota);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLogoEmpresa;
        TextView textEmpresa, textStatusPedido, textDataPedido, textQtdItemPedido,
        textNomeItemPedido, textMaisitens;
        Button btnAjuda, btnDetalhes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLogoEmpresa = itemView.findViewById(R.id.img_logo_empresa);
            textEmpresa = itemView.findViewById(R.id.text_empresa);
            textStatusPedido = itemView.findViewById(R.id.text_status_pedido);
            textDataPedido = itemView.findViewById(R.id.text_data_pedido);
            textQtdItemPedido = itemView.findViewById(R.id.text_qtd_item_pedido);
            textNomeItemPedido = itemView.findViewById(R.id.text_nome_item_pedido);
            textMaisitens = itemView.findViewById(R.id.text_mais_itens);
            btnAjuda = itemView.findViewById(R.id.btn_ajuda);
            btnDetalhes = itemView.findViewById(R.id.btn_detalhes);
        }
    }
}
