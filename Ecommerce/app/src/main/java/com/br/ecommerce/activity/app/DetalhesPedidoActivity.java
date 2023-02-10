package com.br.ecommerce.activity.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.DetalhesPedidoAdapter;
import com.br.ecommerce.databinding.ActivityDetalhesPedidoBinding;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Endereco;
import com.br.ecommerce.model.Pedido;
import com.br.ecommerce.model.TipoValor;

public class DetalhesPedidoActivity extends AppCompatActivity {

    private ActivityDetalhesPedidoBinding binding;
    private DetalhesPedidoAdapter detalhesPedidoAdapter;
    private Pedido pedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        getExtra();
        configCliques();
    }

    private void configRv() {
        binding.rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProdutos.setHasFixedSize(true);
        detalhesPedidoAdapter = new DetalhesPedidoAdapter(pedido.getItemPedidoList(), this);
        binding.rvProdutos.setAdapter(detalhesPedidoAdapter);
    }


    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedidoSelecionado");
            configRv();
            configDados();
        }
    }

    private void configDados() {

        Endereco endereco = pedido.getEndereco();

        StringBuilder enderecoCompleto = new StringBuilder();
        enderecoCompleto.append(endereco.getLogradouro())
                .append(", ")
                .append(endereco.getNumero())
                .append("\n")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getLocalidade())
                .append("/")
                .append(endereco.getUf())
                .append("\n")
                .append("CEP: ")
                .append(endereco.getCep());

        binding.textEnderecoEntrega.setText(enderecoCompleto);
        binding.textNomePagamento.setText(pedido.getPagamento());

        double valorExtra;
        double totalPedido = pedido.getTotal();
        if (pedido.getAcrescimo() > 0) {
            binding.textTipoPagamento.setText(String.valueOf(TipoValor.ACRESCIMO));
            valorExtra = pedido.getAcrescimo();
            totalPedido += valorExtra;
        } else {
            binding.textTipoPagamento.setText(String.valueOf(TipoValor.DESCONTO));
            valorExtra = pedido.getDesconto();
            totalPedido -= valorExtra;
        }

        binding.textValorTipoPagamento.setText(getString(R.string.valor, GetMask.getValor(valorExtra)));
        binding.textValorProdutos.setText(getString(R.string.valor, GetMask.getValor(pedido.getTotal())));
        binding.textValorTotal.setText(getString(R.string.valor, GetMask.getValor(totalPedido)));

    }

    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Detalhes do Pedido");
    }
}