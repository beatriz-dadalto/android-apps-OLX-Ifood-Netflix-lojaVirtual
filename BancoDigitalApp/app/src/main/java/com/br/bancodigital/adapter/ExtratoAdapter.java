package com.br.bancodigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.bancodigital.R;
import com.br.bancodigital.helper.GetMask;
import com.br.bancodigital.model.Extrato;

import java.util.List;

public class ExtratoAdapter extends RecyclerView.Adapter<ExtratoAdapter.MyViewHolder> {

    private List<Extrato> extratoList;
    private Context context;
    private OnClickListener onClick;

    public ExtratoAdapter(List<Extrato> extratoList, Context context, OnClickListener onClick) {
        this.extratoList = extratoList;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_extrato, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Extrato extrato = extratoList.get(position);

        // getOperacao: DEPOSITO ou TRANSFERENCIA ou RECARGA
        String icon = extrato.getOperacao().substring(0, 1);

        holder.textIcon.setText(icon);
        holder.textOperacao.setText(extrato.getOperacao());
        holder.textData.setText(GetMask.getDate(extrato.getData(), 4));
        holder.textValor.setText(context.getString(R.string.text_valor, GetMask.getValor(extrato.getValor())));

        // configurando o background da operacao
        switch (extrato.getOperacao()) {
            case "DEPOSITO":
                holder.textIcon.setBackgroundResource(R.drawable.bg_entrada);
                break;
            case "RECARGA":
                holder.textIcon.setBackgroundResource(R.drawable.bg_saida);
                break;
            case "TRANSFERENCIA":
            case "PAGAMENTO":
                if (extrato.getTipo().equals("ENTRADA")) {
                    holder.textIcon.setBackgroundResource(R.drawable.bg_entrada);
                } else if (extrato.getTipo().equals("SAIDA")){
                    holder.textIcon.setBackgroundResource(R.drawable.bg_saida);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return extratoList.size();
    }

    public interface OnClickListener {
        void onClick(Extrato extrato);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textIcon;
        TextView textOperacao;
        TextView textData;
        TextView textValor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textIcon = itemView.findViewById(R.id.textIcon);
            textOperacao = itemView.findViewById(R.id.textOperacao);
            textData = itemView.findViewById(R.id.textData);
            textValor = itemView.findViewById(R.id.textValor);
        }
    }
}
