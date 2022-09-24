package com.example.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.CarrinhoAdapter;
import com.example.ifoodclone.adapter.EmpresaPedidoAdapter;
import com.example.ifoodclone.adapter.ItemDetalhePedidoAdapter;
import com.example.ifoodclone.adapter.UsuarioPedidoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.Pedido;
import com.example.ifoodclone.model.StatusPedido;
import com.example.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PedidoDetalheActivity extends AppCompatActivity {

    private RecyclerView rv_item_pedido;
    private ItemDetalhePedidoAdapter itemDetalhePedidoAdapter;

    private TextView text_endereco;
    private TextView text_forma_pagamento;
    private TextView text_subtotal;
    private TextView text_taxa_entrega;
    private TextView text_total;
    private TextView text_user;
    private TextView text_status_pedido;

    private ImageView img_log_empresa;
    private ImageView img_status_pedido;
    private CardView card_img_empresa;

    private Pedido pedido;
    private String acesso = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalhe);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            pedido = (Pedido) bundle.getSerializable("pedidoSelecionado");
            acesso = (String) bundle.getSerializable("acesso");

            condigDados();
        }

        condigDados();

        configRv();

        configCliques();

    }

    private void recuperaCliente(){
        DatabaseReference clienteRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(pedido.getIdCliente());
        clienteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                card_img_empresa.setVisibility(View.GONE);
                text_user.setText(usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void recuperaEmpresa(){
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(pedido.getIdEmpresa());
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empresa empresa = snapshot.getValue(Empresa.class);
                text_user.setText(empresa.getNome());
                Picasso.get().load(empresa.getUrlLogo()).into(img_log_empresa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configRv(){
        rv_item_pedido.setLayoutManager(new LinearLayoutManager(this));
        rv_item_pedido.setHasFixedSize(true);
        itemDetalhePedidoAdapter = new ItemDetalhePedidoAdapter(pedido.getItemPedidoList(), getBaseContext());
        rv_item_pedido.setAdapter(itemDetalhePedidoAdapter);
    }

    private void condigDados(){

        Endereco endereco = pedido.getEnderecoEntrega();
        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getLogradouro())
                .append("\n")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getMunicipio())
                .append("\n")
                .append(endereco.getReferencia());

        text_endereco.setText(enderecoCompleto);

        text_forma_pagamento.setText(pedido.getFormaPagamento());

        text_subtotal.setText(getString(R.string.text_valor, GetMask.getValor(pedido.getTotalPedido())));
        text_taxa_entrega.setText(getString(R.string.text_valor, GetMask.getValor(pedido.getTaxaEntrega())));
        text_total.setText(getString(R.string.text_valor, GetMask.getValor(pedido.getTotalPedido() + pedido.getTaxaEntrega())));

        if(acesso.equals("usuario")){
            recuperaEmpresa();
        }else if(acesso.equals("empresa")){
            recuperaCliente();
        }

        text_status_pedido.setText(StatusPedido.getStatus(pedido.getStatusPedido()));

        configStatusPedido();

    }

    private void configStatusPedido(){
        switch (pedido.getStatusPedido()){
            case PENDENTE:
            case PREPARACAO:
                img_status_pedido.setImageResource(R.drawable.ic_check_pendente);
                break;
            case SAIU_ENTREGA:
                img_status_pedido.setImageResource(R.drawable.ic_check_transporte);
                break;
            case CANCELADO_EMPRESA:
            case CANCELADO_USUARIO:
                img_status_pedido.setImageResource(R.drawable.ic_check_cancelado);
                break;
            case ENTREGUE:
                img_status_pedido.setImageResource(R.drawable.ic_check_entrege);
                break;
        }
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Detalhes pedido");

        text_endereco = findViewById(R.id.text_endereco);
        text_forma_pagamento = findViewById(R.id.text_forma_pagamento);
        text_subtotal = findViewById(R.id.text_subtotal);
        text_taxa_entrega = findViewById(R.id.text_taxa_entrega);
        text_total = findViewById(R.id.text_total);
        rv_item_pedido = findViewById(R.id.rv_item_pedido);
        text_user = findViewById(R.id.text_user);
        img_log_empresa = findViewById(R.id.img_log_empresa);
        img_status_pedido = findViewById(R.id.img_status_pedido);
        text_status_pedido = findViewById(R.id.text_status_pedido);
        card_img_empresa = findViewById(R.id.card_img_empresa);
    }

}