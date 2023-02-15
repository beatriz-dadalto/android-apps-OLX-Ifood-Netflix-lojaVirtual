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
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Produto;
import com.bumptech.glide.Glide;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHloder> {

    private List<ItemPedido> itemPedidoList;
    private ItemPedidoDAO itemPedidoDAO;
    private Context context;
    private OnClick onClick;

    public CarrinhoAdapter(List<ItemPedido> itemPedidoList, ItemPedidoDAO itemPedidoDAO, Context context, OnClick onClick) {
        this.itemPedidoList = itemPedidoList;
        this.itemPedidoDAO = itemPedidoDAO;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_carrinho, parent, false);
        return new MyViewHloder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHloder holder, int position) {

        ItemPedido itemPedido = itemPedidoList.get(position);
        Produto produto = itemPedidoDAO.getProduto(itemPedido.getId());

        holder.textTitulo.setText(produto.getTitulo());
        holder.textQuantidade.setText(String.valueOf(itemPedido.getQuantidade()));
        holder.textValor.setText(context.getString(R.string.valor, GetMask.getValor(itemPedido.getValor() * itemPedido.getQuantidade())));

        Glide
                .with(context)
                .load(produto.getUrlsImagens().get(0).getCaminhoImagem())
                .centerCrop()
                .into(holder.imgProduto);

        holder.itemView.setOnClickListener(v -> onClick.onClickListener(position, "detalhe"));
        holder.imgRemover.setOnClickListener(v -> onClick.onClickListener(position, "remover"));
        holder.ibMenos.setOnClickListener(v -> onClick.onClickListener(position, "menos"));
        holder.ibMais.setOnClickListener(v -> onClick.onClickListener(position, "mais"));
    }

    @Override
    public int getItemCount() {
        return itemPedidoList.size();
    }

    public interface OnClick {
        void onClickListener(int position, String operacao);
    }

    static class MyViewHloder extends RecyclerView.ViewHolder {

        ImageView imgProduto, imgRemover;
        ImageButton ibMenos, ibMais;
        TextView textTitulo, textValor, textQuantidade;

        public MyViewHloder(@NonNull View itemView) {
            super(itemView);

            imgProduto = itemView.findViewById(R.id.imgProduto);
            imgRemover = itemView.findViewById(R.id.imgRemover);
            ibMenos = itemView.findViewById(R.id.ibMenos);
            ibMais = itemView.findViewById(R.id.ibMais);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textValor = itemView.findViewById(R.id.textValor);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
        }
    }
}
