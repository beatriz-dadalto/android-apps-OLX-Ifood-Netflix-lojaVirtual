package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.biamailov3.ifoodclone.DAO.EmpresaDAO;
import com.biamailov3.ifoodclone.DAO.EntregaDAO;
import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.CarrinhoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.biamailov3.ifoodclone.model.Pagamento;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.Produto;
import com.biamailov3.ifoodclone.model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class UsuarioResumoPedidoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener {

    private final int REQUEST_ENDERECO = 200;
    private final int REQUEST_PAGAMENTO = 300;

    private final List<ItemPedido> itemPedidoList = new ArrayList<>();
    private RecyclerView rvProdutos;
    private CarrinhoAdapter carrinhoAdapter;

    private TextView textEndereco;
    private TextView textFormaPagamento;
    private TextView textSubtotal;
    private TextView textTaxaEntrega;
    private TextView textTotal;

    private Endereco endereco;
    private EntregaDAO entregaDAO;
    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;

    private String pagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_resumo_pedido);

        entregaDAO = new EntregaDAO(this);
        itemPedidoDAO = new ItemPedidoDAO(this);
        empresaDAO = new EmpresaDAO(this);

        iniciarComponentes();
        configDados();
        configRv();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.btn_finalizar).setOnClickListener(view -> finalizarPedido());
    }

    private void finalizarPedido() {

        double subtotal = itemPedidoDAO.getTotal();
        double taxaEntrega = empresaDAO.getEmpresa().getTaxaEntrega();
        double total = subtotal + taxaEntrega;

        Pedido pedido = new Pedido();

        pedido.setIdCliente(FirebaseHelper.getIdFirebase());
        pedido.setIdEmpresa(empresaDAO.getEmpresa().getId());
        pedido.setFormaPagamento(pagamento);
        pedido.setStatusPedido(StatusPedido.PENDENTE);
        pedido.setItemPedidoList(itemPedidoDAO.getList());
        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setSubTotal(subtotal);
        pedido.setTotalPedido(total);
        pedido.setEnderecoEntrega(entregaDAO.getEndereco());
        pedido.salvar();

        itemPedidoDAO.limparCarrinho();

        Intent intent = new Intent(this, UsuarioHomeActivity.class);
        // inciar uma activity e fechar as anteriores
        getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

    private void configRv() {
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rvProdutos.setAdapter(carrinhoAdapter);
    }

    private void configDados() {

        endereco = entregaDAO.getEndereco();
        pagamento = entregaDAO.getEntrega().getFormaPagamento();

        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getLogradouro())
                .append("\n")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getMunicipio())
                .append("\n")
                .append(endereco.getReferencia());

        textEndereco.setText(enderecoCompleto);
        textFormaPagamento.setText(pagamento);

        configSaldo();
    }

    private void configSaldo() {
        double subTotal = 0.0;
        double taxaEntrega = 0.0;
        double total = 0.0;

        if (!itemPedidoDAO.getList().isEmpty()) {
            subTotal = itemPedidoDAO.getTotal();
            taxaEntrega = empresaDAO.getEmpresa().getTaxaEntrega();
            total = subTotal + taxaEntrega;
        }

        textSubtotal.setText(getString(R.string.text_valor, GetMask.getValor(subTotal)));
        textTaxaEntrega.setText(getString(R.string.text_valor, GetMask.getValor(taxaEntrega)));
        textTotal.setText(getString(R.string.text_valor, GetMask.getValor(total)));
    }

    public void alterarEndereco(View view) {
        Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
        startActivityForResult(intent, REQUEST_ENDERECO);
    }

    public void alterarPagamento(View view) {
        Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
        startActivityForResult(intent, REQUEST_PAGAMENTO);
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Resumo do pedido");

        textEndereco = findViewById(R.id.text_endereco);
        textFormaPagamento = findViewById(R.id.text_forma_pagamento);
        textSubtotal = findViewById(R.id.text_subtotal);
        textTaxaEntrega = findViewById(R.id.text_taxa_entrega);
        textTotal = findViewById(R.id.text_total);
        rvProdutos = findViewById(R.id.rv_produtos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ENDERECO) {
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                entregaDAO.atualizarEndereco(endereco);
                configDados();
            } else if (requestCode == REQUEST_PAGAMENTO) {
                Pagamento formaPagamento = (Pagamento) data.getSerializableExtra("pagamentoSelecionado");
                pagamento = formaPagamento.getDescricao();
                entregaDAO.salvarPagamento(pagamento);
                configDados();
            }
        }
    }

    @Override
    public void onClick(ItemPedido itemPedido) {

    }
}