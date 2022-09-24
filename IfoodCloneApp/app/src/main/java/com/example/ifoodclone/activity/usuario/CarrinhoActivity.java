package com.example.ifoodclone.activity.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.DAO.EmpresaDAO;
import com.example.ifoodclone.DAO.EntregaDAO;
import com.example.ifoodclone.DAO.ItemPedidoDAO;
import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.autenticacao.LoginActivity;
import com.example.ifoodclone.adapter.CarrinhoAdapter;
import com.example.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.ItemPedido;
import com.example.ifoodclone.model.Pagamento;
import com.example.ifoodclone.model.Produto;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    private final List<Produto> produtoList = new ArrayList<>();
    private List<ItemPedido> itemPedidoList = new ArrayList<>();

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;

    private RecyclerView rv_produtos;
    private RecyclerView rv_add_mais;
    private LinearLayout ll_add_mais;
    private LinearLayout ll_btn_add_mais;

    private Endereco endereco;
    private LinearLayout ll_endereco;
    private TextView text_logradouro;
    private TextView text_referencia;

    private String pagamento;
    private TextView text_forma_pagamento;

    private Button btn_add_mais;
    private TextView text_subtotal;
    private TextView text_taxa_entrega;
    private TextView text_total;

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;
    private EntregaDAO entregaDAO;

    private Button btn_continuar;
    private Button text_escolher_pagamento;

    private BottomSheetDialog bottomSheetDialog;

    private int quantidade = 0;

    private Produto produto;
    private ItemPedido itemPedido;

    private TextView text_nome_produto;
    private ImageButton ib_remove;
    private ImageButton ib_add;
    private TextView text_qtd_produto;
    private TextView text_total_produto_dialog;
    private TextView text_atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        itemPedidoList = itemPedidoDAO.getList();
        empresaDAO = new EmpresaDAO(getBaseContext());
        entregaDAO = new EntregaDAO(getBaseContext());

        iniciaComponentes();

        configCliques();

        configRv();

        recuperaIdsItensAddMais();

        recuperaEnderecos();

        configSaldoCarrinho();

        configPagamento();

    }


    private void showBottomSheet(){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_item_carrinho, null);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        text_nome_produto = view.findViewById(R.id.text_nome_produto);
        ib_remove = view.findViewById(R.id.ib_remove);
        ib_add = view.findViewById(R.id.ib_add);
        text_qtd_produto = view.findViewById(R.id.text_qtd_produto);
        text_total_produto_dialog = view.findViewById(R.id.text_total_produto_dialog);
        text_atualizar = view.findViewById(R.id.text_atualizar);

        produto = new Produto();
        produto.setIdLocal(itemPedido.getId());
        produto.setNome(itemPedido.getItem());
        produto.setId(itemPedido.getIdItem());
        produto.setValor(itemPedido.getValor());
        produto.setUrlImagem(itemPedido.getUrlImagem());

        ib_add.setOnClickListener(v -> addQtdItem());
        ib_remove.setOnClickListener(v -> delQtdItem());

        text_qtd_produto.setText(String.valueOf(itemPedido.getQuantidade()));
        text_nome_produto.setText(produto.getNome());
        text_total_produto_dialog.setText(getString(R.string.text_valor,
                GetMask.getValor(produto.getValor() * itemPedido.getQuantidade())));
        quantidade = itemPedido.getQuantidade();


    }

    private void configValoresDialog(){
        text_qtd_produto.setText(String.valueOf(quantidade));
        text_total_produto_dialog.setText(getString(R.string.text_valor,
                GetMask.getValor(produto.getValor() * quantidade)));
    }

    private void addQtdItem(){
        quantidade++;
        if(quantidade == 1){
            ib_remove.setImageResource(R.drawable.ic_remove_red);
            text_total_produto_dialog.setVisibility(View.VISIBLE);
            text_atualizar.setText("Atualizar");
        }

        text_atualizar.setOnClickListener(v -> {
            atualizarItem();
        });

        configValoresDialog();

    }

    private void delQtdItem(){
        if(quantidade > 0){
            quantidade--;

            if(quantidade == 0){ // Remover do carrinho

                ib_remove.setImageResource(R.drawable.ic_remove);
                text_total_produto_dialog.setVisibility(View.GONE);
                text_atualizar.setText("Remover");
                text_atualizar.setGravity(Gravity.CENTER);

                text_atualizar.setOnClickListener(v -> {

                    itemPedidoDAO.remover(itemPedido.getId());
                    itemPedidoList.remove(itemPedido);

                    addMaisList();

                    configSaldoCarrinho();

                    configBtnAddMais();

                    carrinhoAdapter.notifyDataSetChanged();
                    bottomSheetDialog.dismiss();

                });

            }else {
                text_atualizar.setOnClickListener(v -> {
                    atualizarItem();
                });
            }

        }

        configValoresDialog();

    }

    private void configBtnAddMais(){
        if(itemPedidoList.isEmpty()){
            ll_btn_add_mais.setVisibility(View.GONE);
        }else {
            ll_btn_add_mais.setVisibility(View.VISIBLE);
        }
    }

    private void atualizarItem(){
        itemPedido.setQuantidade(quantidade);
        itemPedidoDAO.atualizar(itemPedido);
        carrinhoAdapter.notifyDataSetChanged();

        configSaldoCarrinho();
        bottomSheetDialog.dismiss();
    }

    private void configSaldoCarrinho(){
        double subTotal = 0;
        double taxaEntrega = 0;
        double total = 0;

        if(!itemPedidoDAO.getList().isEmpty()){
            subTotal = itemPedidoDAO.getTotal();
            taxaEntrega = empresaDAO.getEmpresa().getTaxaEntrega();
            total = subTotal + taxaEntrega;
        }

        text_subtotal.setText(getString(R.string.text_valor, GetMask.getValor(subTotal)));
        text_taxa_entrega.setText(getString(R.string.text_valor, GetMask.getValor(taxaEntrega)));
        text_total.setText(getString(R.string.text_valor, GetMask.getValor(total)));

    }

    private void recuperaEnderecos(){
        if(FirebaseHelper.getAutenticado() && entregaDAO.getEndereco() == null){
            DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                    .child("enderecos")
                    .child(FirebaseHelper.getIdFirebase());
            enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            endereco = ds.getValue(Endereco.class);
                        }

                        entregaDAO.salvarEndereco(endereco);

                        configEndereco();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            configEndereco();
        }
    }

    private void configEndereco(){
        endereco = entregaDAO.getEndereco();
        if(endereco != null){
            text_logradouro.setText(endereco.getLogradouro());
            text_referencia.setText(endereco.getReferencia());
            ll_endereco.setVisibility(View.VISIBLE);
        }

        configStatus();
    }

    private void configPagamento(){
        pagamento = entregaDAO.getEntrega().getFormaPagamento();
        if(pagamento != null && !pagamento.isEmpty()){
            text_forma_pagamento.setText(pagamento);
            configStatus();
        }
    }

    private void configStatus(){
        if(endereco == null){
            btn_continuar.setText("Selecione o endereço");
        }else {
            if(pagamento == null){
                btn_continuar.setText("Selecione a forma de pagamento");
            }else {
                btn_continuar.setText("Continuar");
                text_escolher_pagamento.setText("Trocar");
            }
        }
    }

    private void configLayoutAddMais(){
        if(produtoList.isEmpty()){
            ll_add_mais.setVisibility(View.GONE);
        }else {
            ll_add_mais.setVisibility(View.VISIBLE);
        }
    }

    private void addMaisList(){
        boolean contem = false;
        if(produtoList.size() == 0){
            produtoList.add(produto);
        }else {
            for (Produto prod : produtoList){
                if(prod.getId().equals(produto.getId())){
                    contem = true;
                    break;
                }
            }

            if(!contem){
                produtoList.add(produto);
            }

        }

        configLayoutAddMais();

        produtoCarrinhoAdapter.notifyDataSetChanged();

    }

    private void configCliques() {
        btn_add_mais.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });

        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_limpar).setOnClickListener(v -> {
            itemPedidoDAO.limparCarrinho();
            finish();
        });

        btn_continuar.setOnClickListener(v -> continuar());

        ll_endereco.setOnClickListener(v -> {
            Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
            startActivityForResult(intent, REQUEST_ENDERECO);
        });

        text_escolher_pagamento.setOnClickListener(v -> {
            Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
            startActivityForResult(intent, REQUEST_PAGAMENTO);
        });

    }

    private void continuar(){
        if(FirebaseHelper.getAutenticado()){
            if(endereco == null){
                Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            }else {
                if(pagamento == null){
                    Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
                    startActivityForResult(intent, REQUEST_PAGAMENTO);
                }else {
                    startActivity(new Intent(this, UsuarioResumoPedidoActivity.class));
                }
            }
        }else {
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
                    configLayoutAddMais();
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
        rv_produtos.setLayoutManager(new LinearLayoutManager(this));
        rv_produtos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoList, getBaseContext(), this);
        rv_produtos.setAdapter(carrinhoAdapter);

        rv_add_mais.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_add_mais.setHasFixedSize(true);
        produtoCarrinhoAdapter = new ProdutoCarrinhoAdapter(produtoList, getBaseContext(), this);
        rv_add_mais.setAdapter(produtoCarrinhoAdapter);
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Sacola");

        rv_produtos = findViewById(R.id.rv_produtos);
        rv_add_mais = findViewById(R.id.rv_add_mais);
        ll_add_mais = findViewById(R.id.ll_add_mais);
        ll_btn_add_mais = findViewById(R.id.ll_btn_add_mais);
        btn_continuar = findViewById(R.id.btn_continuar);
        text_forma_pagamento = findViewById(R.id.text_forma_pagamento);
        text_escolher_pagamento = findViewById(R.id.text_escolher_pagamento);

        btn_add_mais = findViewById(R.id.btn_add_mais);
        text_subtotal = findViewById(R.id.text_subtotal);
        text_taxa_entrega = findViewById(R.id.text_taxa_entrega);
        text_total = findViewById(R.id.text_total);

        ll_endereco = findViewById(R.id.ll_endereco);
        text_logradouro = findViewById(R.id.text_logradouro);
        text_referencia = findViewById(R.id.text_referencia);
    }

    @Override
    public void OnClick(ItemPedido itemPedido) { // RV principal
        this.itemPedido = itemPedido;
        showBottomSheet();
    }

    @Override
    public void OnClick(Produto produto) { // Peça mais

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(1);
        itemPedido.setItem(produto.getNome());
        itemPedido.setIdItem(produto.getId());
        itemPedido.setValor(produto.getValor());
        itemPedido.setUrlImagem(produto.getUrlImagem());

        long id = itemPedidoDAO.salvar(itemPedido);
        itemPedido.setId(id);

        itemPedidoList.add(itemPedido);
        carrinhoAdapter.notifyDataSetChanged();

        produtoList.remove(produto);
        produtoCarrinhoAdapter.notifyDataSetChanged();

        configSaldoCarrinho();

        configLayoutAddMais();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_LOGIN){
                Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            }else if(requestCode == REQUEST_ENDERECO){
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                if(entregaDAO.getEndereco() == null){
                    entregaDAO.salvarEndereco(endereco);
                }else {
                    entregaDAO.atualizarEndereco(endereco);
                }
                configEndereco();
            }else if(requestCode == REQUEST_PAGAMENTO){
                Pagamento formaPagamento = (Pagamento) data.getSerializableExtra("pagamentoSelecionado");
                pagamento = formaPagamento.getDescricao();
                entregaDAO.salvarPagamento(pagamento);
                configPagamento();
            }
        }
    }
}