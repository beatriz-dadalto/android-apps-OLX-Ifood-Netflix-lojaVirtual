package com.br.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Pedido;
import com.br.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetalhesPedidoAdapter extends RecyclerView.Adapter<DetalhesPedidoAdapter.MyViewHloder> {

    private List<ItemPedido> itemPedidoList;
    private Context context;

    public DetalhesPedidoAdapter(List<ItemPedido> itemPedidoList, Context context) {
        this.itemPedidoList = itemPedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_detalhe_pedido, parent, false);
        return new MyViewHloder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHloder holder, int position) {

        ItemPedido itemPedido = itemPedidoList.get(position);

        recuperaProduto(itemPedido.getIdProduto(), holder);

        holder.textQuantidade.setText(String.valueOf(itemPedido.getQuantidade()));
        holder.textValor.setText(context.getString(R.string.valor, GetMask.getValor(itemPedido.getValor() * itemPedido.getQuantidade())));
    }

    private void recuperaProduto(String idProduto, MyViewHloder holder) {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(idProduto);
        produtoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Produto produto = snapshot.getValue(Produto.class);
                    holder.textTitulo.setText(produto.getTitulo());
                    Picasso.get().load(produto.getUrlsImagens().get(0).getCaminhoImagem()).into(holder.imgProduto);
                } else {
                    holder.textTitulo.setText("Produto não localizado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemPedidoList.size();
    }

    static class MyViewHloder extends RecyclerView.ViewHolder {

        ImageView imgProduto;
        TextView textTitulo, textValor, textQuantidade;

        public MyViewHloder(@NonNull View itemView) {
            super(itemView);

            imgProduto = itemView.findViewById(R.id.imgProduto);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textValor = itemView.findViewById(R.id.textValor);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
        }
    }
}
