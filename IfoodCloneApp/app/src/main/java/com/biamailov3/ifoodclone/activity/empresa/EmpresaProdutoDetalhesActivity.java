package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.DAO.EmpresaDAO;
import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.activity.usuario.CarrinhoActivity;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.biamailov3.ifoodclone.model.Produto;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EmpresaProdutoDetalhesActivity extends AppCompatActivity {

    private ImageView imgProduto;
    private TextView textProduto;
    private TextView textDescricao;
    private TextView textValor;
    private TextView textValorAntigo;
    private TextView textEmpresa;
    private TextView textTempoEntrega;

    private ConstraintLayout btnAdicionar;
    private TextView textQtdProduto;
    private TextView textTotalProduto;
    private ImageButton btnRemover;
    private ImageButton btnAdd;

    private Produto produto;
    private Empresa empresa;

    private EmpresaDAO empresaDAO;
    private ItemPedidoDAO itemPedidoDAO;

    private int quantidade = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_produto_detalhes);

        empresaDAO = new EmpresaDAO(getBaseContext());
        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());

        iniciarComponentes();

        // dados vindos de ProdutoCardapioAdapter
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            produto = (Produto) bundle.getSerializable("produtoSelecionado");

            recuperarEmpresa();
            configDados();
        }

        configCliques();
    }

    private void addItemCarrinho() {
        if (empresaDAO.getEmpresa() != null) {
            if (produto.getIdEmpresa().equals(empresaDAO.getEmpresa().getId())) {
                salvarProduto();
            } else {
                Snackbar.make(btnAdicionar, "Empresas diferentes.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            salvarProduto();
        }
    }

    private void salvarProduto() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(quantidade);
        itemPedido.setUrlImagem(produto.getUrlImagem());
        itemPedido.setValor(produto.getValor());
        itemPedido.setIdItem(produto.getId());
        itemPedido.setItem(produto.getNome());

        itemPedidoDAO.salvar(itemPedido);

        if (empresaDAO.getEmpresa() == null) empresaDAO.salvar(empresa);

        startActivity(new Intent(this, CarrinhoActivity.class));
    }

    private void addQtdItem() {
        quantidade++;
        btnRemover.setImageResource(R.drawable.ic_remove_red);

        // valor * quantidade
        atualizarSaldo();
    }

    private void delQtdItem() {
        if (quantidade > 1) {
            quantidade--;

            if (quantidade == 1) {
                btnRemover.setImageResource(R.drawable.ic_remove);
            }

            // valor * quantidade
            atualizarSaldo();
        }
    }

    private void atualizarSaldo() {
        textQtdProduto.setText(String.valueOf(quantidade));
        textTotalProduto.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValor() * quantidade)));
    }

    private void recuperarEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(produto.getIdEmpresa());
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);

                textEmpresa.setText(empresa.getNome());
                textTempoEntrega.setText(empresa.getTempoMinEntrega() + "-" + empresa.getTempoMaxEntrega() + " min");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        Picasso.get().load(produto.getUrlImagem()).into(imgProduto);
        textProduto.setText(produto.getNome());
        textDescricao.setText(produto.getDescricao());
        textValor.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValor())));
        textTotalProduto.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValor() * quantidade)));

        if (produto.getValorAntigo() > 0) {
            textValorAntigo.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValorAntigo())));
        } else {
            textValorAntigo.setText("");
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.btn_adicionar).setOnClickListener(v -> addItemCarrinho());
        btnAdd.setOnClickListener(view -> addQtdItem());
        btnRemover.setOnClickListener(view -> delQtdItem());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Detalhes");

        imgProduto = findViewById(R.id.img_produto);
        textProduto = findViewById(R.id.text_produto);
        textDescricao = findViewById(R.id.text_descricao);
        textValor = findViewById(R.id.text_valor);
        textValorAntigo = findViewById(R.id.text_valor_antigo);
        textEmpresa = findViewById(R.id.text_empresa);
        textTempoEntrega = findViewById(R.id.text_tempo_entrega);

        btnAdicionar = findViewById(R.id.btn_adicionar);
        textQtdProduto = findViewById(R.id.text_qdt_produto);
        textTotalProduto = findViewById(R.id.text_total_produto);
        btnRemover = findViewById(R.id.btn_remover);
        btnAdd = findViewById(R.id.btn_add);
    }
}