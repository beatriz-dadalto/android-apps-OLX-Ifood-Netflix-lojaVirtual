package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.DAO.EmpresaDAO;
import com.biamailov3.ifoodclone.DAO.EntregaDAO;
import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.autenticacao.LoginActivity;
import com.biamailov3.ifoodclone.adapter.CarrinhoAdapter;
import com.biamailov3.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.biamailov3.ifoodclone.model.Pagamento;
import com.biamailov3.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener, ProdutoCarrinhoAdapter.OnClickListener {

    private final int REQUEST_LOGIN = 100;
    private final int REQUEST_ENDERECO = 200;
    private final int REQUEST_PAGAMENTO = 300;

    private List<Produto> produtoList = new ArrayList<>();

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;

    private RecyclerView rvProdutos;
    private RecyclerView rvAddMais;
    private LinearLayout llAddMais;
    private Button btnContinuar;
    private Button textEscolherPagamento;

    private Endereco endereco;
    private LinearLayout llEndereco;
    private TextView textLogradouro;
    private TextView textReferencia;

    private Pagamento pagamento;
    private TextView textFormaPagamento;

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;
    private EntregaDAO entregaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        empresaDAO = new EmpresaDAO(getBaseContext());
        entregaDAO = new EntregaDAO(getBaseContext());

        iniciarComponentes();

        configCliques();

        configRv();

        recuperaIdsItensAddMais();
        recuperarEnderecos();
    }

    private void recuperarEnderecos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                    .child("enderecos")
                    .child(FirebaseHelper.getIdFirebase());
            enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            endereco = ds.getValue(Endereco.class);
                        }
                    }

                    if (endereco != null) entregaDAO.salvarEndereco(endereco);
                    configEndereco();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            configEndereco();
        }
    }

    private void configEndereco() {
        if (endereco != null) {
            textLogradouro.setText(endereco.getLogradouro());
            textReferencia.setText(endereco.getReferencia());
            llEndereco.setVisibility(View.VISIBLE);
        }
        configStatus();
    }

    private void configPagamento() {
        textFormaPagamento.setText(pagamento.getDescricao());
        configStatus();
    }

    private void configStatus() {
        if (endereco == null) {
            btnContinuar.setText("Selecione o endereço");
        } else {
            if (pagamento == null) {
                btnContinuar.setText("Selecione a forma de pagamento");
            } else {
                btnContinuar.setText("Continuar");
                textEscolherPagamento.setText("Trocar");
            }
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_limpar).setOnClickListener(view -> {
            itemPedidoDAO.limparCarrinho();
            finish();
        });

        btnContinuar.setOnClickListener(view -> continuar());

        llEndereco.setOnClickListener(view -> {
            Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
            startActivityForResult(intent, REQUEST_ENDERECO);
        });

        textEscolherPagamento.setOnClickListener(view -> {
            Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
            startActivityForResult(intent, REQUEST_PAGAMENTO);
        });
    }

    private void continuar() {
        if (FirebaseHelper.getAutenticado()) {
            if (endereco == null) {
                Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            } else {
                if (pagamento == null) {
                    Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
                    startActivityForResult(intent, REQUEST_PAGAMENTO);
                }
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void recuperaIdsItensAddMais(){
        DatabaseReference addMaisRef = FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(empresaDAO.getEmpresa().getId());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> idsItensList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String idProduto = ds.getValue(String.class);
                        idsItensList.add(idProduto);
                    }

                    recuperaProdutos(idsItensList);

                }else {
                    llAddMais.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaProdutos(List<String> idsItensList) {
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(empresaDAO.getEmpresa().getId());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Produto produto = ds.getValue(Produto.class);
                    if(idsItensList.contains(produto.getId())) produtoList.add(produto);
                }

                Collections.reverse(produtoList);
                produtoCarrinhoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv(){
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rvProdutos.setAdapter(carrinhoAdapter);

        rvAddMais.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAddMais.setHasFixedSize(true);
        produtoCarrinhoAdapter = new ProdutoCarrinhoAdapter(produtoList, getBaseContext(), this);
        rvAddMais.setAdapter(produtoCarrinhoAdapter);
    }

    private void iniciarComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Sacola");

        rvProdutos = findViewById(R.id.rv_produtos);
        rvAddMais = findViewById(R.id.rv_add_mais);
        llAddMais = findViewById(R.id.ll_add_mais);
        llEndereco = findViewById(R.id.ll_endereco);
        textLogradouro = findViewById(R.id.text_logradouro);
        textReferencia = findViewById(R.id.text_referencia);
        btnContinuar = findViewById(R.id.btn_continuar);
        textFormaPagamento = findViewById(R.id.text_forma_pagamento);
        textEscolherPagamento = findViewById(R.id.text_escolher_pagamento);
    }

    @Override
    public void onClick(ItemPedido itemPedido) { // RV principal
        Toast.makeText(this, itemPedido.getItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Produto produto) { // Peça mais

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            } else if (requestCode == REQUEST_ENDERECO){
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                configEndereco();
                if (entregaDAO.getEndereco() == null) {
                    entregaDAO.salvarEndereco(endereco);
                } else {
                    entregaDAO.atualizarEndereco(endereco);
                }
            } else if (requestCode == REQUEST_PAGAMENTO){
                pagamento = (Pagamento) data.getSerializableExtra("pagamentoSelecionado");
                configPagamento();
                entregaDAO.salvarPagamento(pagamento.getDescricao());
            }
        }

    }
}