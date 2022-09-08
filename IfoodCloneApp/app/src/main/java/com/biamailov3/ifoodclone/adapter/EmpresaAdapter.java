package com.biamailov3.ifoodclone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.MyViewHolder> {

    private List<Empresa> empresaList;
    private OnClickListener onClickListener;
    private Context context;

    public EmpresaAdapter(List<Empresa> empresaList, OnClickListener onClickListener, Context context) {
        this.empresaList = empresaList;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Empresa empresa = empresaList.get(position);

        Picasso.get().load(empresa.getUrlLogo()).into(holder.imgLogoEmpresa);
        holder.textEmpresa.setText(empresa.getNome());
        holder.textCategoriaEmpresa.setText(empresa.getCategoria());
        holder.textTempoMinimo.setText(empresa.getTempoMinEntrega() + "-");
        holder.textTempoMaximo.setText(empresa.getTempoMaxEntrega() + " min");
        if (empresa.getTaxaEntrega() > 0) {
            holder.textTaxaEntrega.setText(context.getString(R.string.text_valor, GetMask.getValor(empresa.getTaxaEntrega())));
        } else {
            holder.textTaxaEntrega.setTextColor(Color.parseColor("#2ED67E"));
            holder.textTaxaEntrega.setText("Entrega Grátis");
        }
        // quando clicar na empresa vai acessar o cardapio
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(empresa));
    }

    @Override
    public int getItemCount() {
        return empresaList.size();
    }

    public interface OnClickListener {
        void onClick(Empresa empresa);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLogoEmpresa;
        TextView textEmpresa, textCategoriaEmpresa, textTempoMinimo, textTempoMaximo, textTaxaEntrega;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLogoEmpresa = itemView.findViewById(R.id.img_logo_empresa);
            textEmpresa = itemView.findViewById(R.id.text_empresa);
            textCategoriaEmpresa = itemView.findViewById(R.id.text_categoria_empresa);
            textTempoMinimo = itemView.findViewById(R.id.text_tempo_minimo);
            textTempoMaximo = itemView.findViewById(R.id.text_tempo_maximo);
            textTaxaEntrega = itemView.findViewById(R.id.text_taxa_entrega);
        }
    }
}
