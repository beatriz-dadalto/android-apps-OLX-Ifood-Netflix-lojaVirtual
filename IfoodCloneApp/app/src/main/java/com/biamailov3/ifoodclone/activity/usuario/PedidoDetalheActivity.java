package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.ItemDetalhePedidoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.Pedido;
import com.biamailov3.ifoodclone.model.StatusPedido;
import com.biamailov3.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PedidoDetalheActivity extends AppCompatActivity {

    private RecyclerView rvItemPedido;
    private ItemDetalhePedidoAdapter itemDetalhePedidoAdapter;

    private TextView textEndereco;
    private TextView textFormaPagamento;
    private TextView textSubtotal;
    private TextView textTaxaEntrega;
    private TextView textTotal;
    private TextView textUser;
    private TextView textStatusPedido;
    private ImageView imgLogoEmpresa;
    private ImageView imgStatusPedido;
    private CardView cardImgEmpresa;

    private Pedido pedido;
    private String acesso = ""; // eh user ou empresa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalhe);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedidoSelecionado");
            acesso = (String) bundle.getSerializable("acesso");

            configDados();
        }

        configDados();
        configRv();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void recuperarEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(pedido.getIdEmpresa());
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empresa empresa = snapshot.getValue(Empresa.class);
                textUser.setText(empresa.getNome());
                Picasso.get().load(empresa.getUrlLogo()).into(imgLogoEmpresa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarCliente() {
        DatabaseReference clienteRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(pedido.getIdCliente());
        clienteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                cardImgEmpresa.setVisibility(View.GONE);
                textUser.setText(usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configStatusPedido() {
        switch (pedido.getStatusPedido()) {
            case PENDENTE:
            case PREPARACAO:
                imgStatusPedido.setImageResource(R.drawable.ic_check_pendente);
                break;
            case SAIU_ENTREGA:
                imgStatusPedido.setImageResource(R.drawable.ic_check_transporte);
                break;
            case CANCELADO_EMPRESA:
            case CANCELADO_USUARIO:
                imgStatusPedido.setImageResource(R.drawable.ic_check_cancelado);
                break;
            case ENTREGUE:
                imgStatusPedido.setImageResource(R.drawable.ic_check_entregue);
                break;
        }
    }

    private void configRv() {
        rvItemPedido.setLayoutManager(new LinearLayoutManager(this));
        rvItemPedido.setHasFixedSize(true);
        itemDetalhePedidoAdapter = new ItemDetalhePedidoAdapter(pedido.getItemPedidoList(), getBaseContext());
        rvItemPedido.setAdapter(itemDetalhePedidoAdapter);
    }

    private void configDados() {
        Endereco endereco = pedido.getEnderecoEntrega();

        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getLogradouro())
                .append("\n")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getMunicipio())
                .append("\n")
                .append(endereco.getReferencia());

        textEndereco.setText(enderecoCompleto);
        textFormaPagamento.setText(pedido.getFormaPagamento());

        textSubtotal.setText(getString(R.string.text_valor,
                GetMask.getValor(pedido.getTotalPedido())));
        textTaxaEntrega.setText(getString(R.string.text_valor,
                GetMask.getValor(pedido.getTaxaEntrega())));
        textTotal.setText(getString(R.string.text_valor,
                GetMask.getValor(pedido.getTotalPedido() + pedido.getTaxaEntrega())));

        if (acesso.equals("usuario")) {
            recuperarEmpresa();
        } else if (acesso.equals("empresa")){
            recuperarCliente();
        }

        textStatusPedido.setText(StatusPedido.getStatus(pedido.getStatusPedido()));
        configStatusPedido();
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Detalhes do pedido");

        textEndereco = findViewById(R.id.text_endereco);
        textFormaPagamento = findViewById(R.id.text_forma_pagamento);
        textSubtotal = findViewById(R.id.text_subtotal);
        textTaxaEntrega = findViewById(R.id.text_taxa_entrega);
        textTotal = findViewById(R.id.text_total);
        rvItemPedido = findViewById(R.id.rv_item_pedido);
        textUser = findViewById(R.id.text_user);
        imgLogoEmpresa = findViewById(R.id.img_logo_empresa);
        imgStatusPedido = findViewById(R.id.img_status_pedido);
        textStatusPedido = findViewById(R.id.text_status_pedido);
        cardImgEmpresa = findViewById(R.id.card_img_empresa);
    }

}