package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Endereco;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.biamailov3.ifoodclone.model.Pagamento;
import com.biamailov3.ifoodclone.model.Produto;
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

    private List<Produto> produtoList = new ArrayList<>();
    private List<ItemPedido> itemPedidoList = new ArrayList<>();

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;

    private RecyclerView rvProdutos;
    private RecyclerView rvAddMais;
    private LinearLayout llAddMais;
    private LinearLayout llBtnAddMais;
    private Button btnContinuar;
    private Button textEscolherPagamento;

    private Endereco endereco;
    private LinearLayout llEndereco;
    private TextView textLogradouro;
    private TextView textReferencia;

    private String pagamento;
    private TextView textFormaPagamento;

    private Button btnAddMais;
    private TextView textSubTotal;
    private TextView textTaxaEntrega;
    private TextView textTotal;

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;
    private EntregaDAO entregaDAO;

    private BottomSheetDialog bottomSheetDialog;

    // iniciar componentes do metodo showBottomSheet
    private TextView textNomeProduto;
    private ImageButton ibRemove;
    private ImageButton ibAdd;
    private TextView textQtdProduto;
    private TextView textTotalProdutoDialog;
    private TextView textAtualizar;

    private Produto produto;
    private ItemPedido itemPedido;

    private int quantidade = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        itemPedidoList = itemPedidoDAO.getList();
        empresaDAO = new EmpresaDAO(getBaseContext());
        entregaDAO = new EntregaDAO(getBaseContext());

        iniciarComponentes();

        configCliques();

        configRv();

        recuperaIdsItensAddMais();
        recuperarEnderecos();
        configSaldoCarrinho();
        configPagamento();
    }

    private void showBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_item_carrinho, null);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        textNomeProduto = view.findViewById(R.id.text_nome_produto);
        ibRemove = view.findViewById(R.id.ib_remove);
        ibAdd = view.findViewById(R.id.ib_add);
        textQtdProduto = view.findViewById(R.id.text_qtd_produto);
        textTotalProdutoDialog = view.findViewById(R.id.text_total_produto_dialog);
        textAtualizar = view.findViewById(R.id.text_atualizar);

        produto = new Produto();
        produto.setIdLocal(itemPedido.getId());
        produto.setNome(itemPedido.getItem());
        produto.setId(itemPedido.getIdItem());
        produto.setValor(itemPedido.getValor());
        produto.setUrlImagem(itemPedido.getUrlImagem());

        ibAdd.setOnClickListener(v -> addQtdItem());
        ibRemove.setOnClickListener(v -> delQtdItem());

        textQtdProduto.setText(String.valueOf(itemPedido.getQuantidade()));
        textNomeProduto.setText(produto.getNome());
        textTotalProdutoDialog.setText(getString(R.string.text_valor,
                GetMask.getValor(produto.getValor() * itemPedido.getQuantidade())));
        quantidade = itemPedido.getQuantidade();
    }

    private void configValoresDialog() {
        textQtdProduto.setText(String.valueOf(quantidade));
        textTotalProdutoDialog.setText(getString(R.string.text_valor,
                GetMask.getValor(produto.getValor() * quantidade)));
    }

    private void addQtdItem() {
        quantidade++;
        if (quantidade == 1) {
            ibRemove.setImageResource(R.drawable.ic_remove_red);
            textTotalProdutoDialog.setVisibility(View.VISIBLE);
            textAtualizar.setText("Atualizar");
            textAtualizar.setGravity(Gravity.CENTER);
        }

        textAtualizar.setOnClickListener(view -> {
            atualizarItem();
        });

        configValoresDialog();
    }

    private  void delQtdItem() {
        if (quantidade > 0) {
            quantidade--;
            if (quantidade == 0 ) {
                // remover item do carrinho
                ibRemove.setImageResource(R.drawable.ic_remove);
                textTotalProdutoDialog.setVisibility(View.GONE);
                textAtualizar.setText("Remover");
                textAtualizar.setGravity(Gravity.CENTER);

                textAtualizar.setOnClickListener(view -> {
                    itemPedidoDAO.remover(itemPedido.getId());
                    itemPedidoList.remove(itemPedido);
                    addMaisList();
                    configSaldoCarrinho();
                    configBtnAddMais();
                    carrinhoAdapter.notifyDataSetChanged();
                    bottomSheetDialog.dismiss();
                });

            } else {
                textAtualizar.setOnClickListener(view -> atualizarItem());
            }
        }

        configValoresDialog();
    }

    private void configBtnAddMais() {
        if (itemPedidoList.isEmpty()) {
            llBtnAddMais.setVisibility(View.GONE);
        } else {
            llBtnAddMais.setVisibility(View.VISIBLE);
        }
    }

    private void atualizarItem() {
        itemPedido.setQuantidade(quantidade);
        itemPedidoDAO.atualizar(itemPedido);
        carrinhoAdapter.notifyDataSetChanged();

        configSaldoCarrinho();
        bottomSheetDialog.dismiss();
    }

    private void configSaldoCarrinho() {
        double subTotal = 0.0;
        double taxaEntrega = 0.0;
        double total = 0.0;

        if (!itemPedidoDAO.getList().isEmpty()) {
            subTotal = itemPedidoDAO.getTotal();
            taxaEntrega = empresaDAO.getEmpresa().getTaxaEntrega();
            total = subTotal + taxaEntrega;
        }

        textSubTotal.setText(getString(R.string.text_valor, GetMask.getValor(subTotal)));
        textTaxaEntrega.setText(getString(R.string.text_valor, GetMask.getValor(taxaEntrega)));
        textTotal.setText(getString(R.string.text_valor, GetMask.getValor(total)));
    }

    private void recuperarEnderecos() {
        if (FirebaseHelper.getAutenticado() && entregaDAO.getEndereco() == null) {
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
        endereco = entregaDAO.getEndereco();
        if (endereco != null) {
            textLogradouro.setText(endereco.getLogradouro());
            textReferencia.setText(endereco.getReferencia());
            llEndereco.setVisibility(View.VISIBLE);
        }
        configStatus();
    }

    private void configPagamento() {
        pagamento = entregaDAO.getEntrega().getFormaPagamento();
        if (pagamento != null && !pagamento.isEmpty()) {
            textFormaPagamento.setText(pagamento);
            configStatus();
        }
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

    private void configLayoutAddMais() {
        if (produtoList.isEmpty()) {
            llAddMais.setVisibility(View.GONE);
        } else {
            llAddMais.setVisibility(View.VISIBLE);
        }
    }

    private void addMaisList() {
        boolean contem = false;
        if (produtoList.size() == 0) {
            produtoList.add(produto);
        } else {
            for (Produto prod : produtoList) {
                if (prod.getId().equals(produto.getId())) {
                    contem = true;
                    break;
                }
            }

            if (!contem) {
                produtoList.add(produto);
            }
        }

        configLayoutAddMais();
        produtoCarrinhoAdapter.notifyDataSetChanged();
    }

    private void configCliques() {

        btnAddMais.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });

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
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoList, getBaseContext(), this);
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
        textSubTotal = findViewById(R.id.text_subtotal);
        textTaxaEntrega = findViewById(R.id.text_taxa_entrega);
        textTotal = findViewById(R.id.text_total);
        btnAddMais = findViewById(R.id.btn_add_mais);
        llBtnAddMais = findViewById(R.id.ll_btn_add_mais);
    }

    @Override
    public void onClick(ItemPedido itemPedido) { // RV principal
        this.itemPedido = itemPedido;
        showBottomSheet();
    }

    @Override
    public void onClick(Produto produto) { // Peça mais
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

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            } else if (requestCode == REQUEST_ENDERECO){
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                if (entregaDAO.getEndereco() == null) {
                    entregaDAO.salvarEndereco(endereco);
                } else {
                    entregaDAO.atualizarEndereco(endereco);
                }
                configEndereco();
            } else if (requestCode == REQUEST_PAGAMENTO){
                Pagamento formaPagamento = (Pagamento) data.getSerializableExtra("pagamentoSelecionado");
                pagamento = formaPagamento.getDescricao();
                entregaDAO.salvarPagamento(pagamento);
                configPagamento();
            }
        }

    }
}