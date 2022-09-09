package com.biamailov3.ifoodclone.activity.empresa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.GetMask;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Produto;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_produto_detalhes);

        iniciarComponentes();

        // dados vindos de ProdutoCardapioAdapter
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            produto = (Produto) bundle.getSerializable("produtoSelecionado");
            
            configDados();
        }
        
        configCliques();
    }

    private void configDados() {
        Picasso.get().load(produto.getUrlImagem()).into(imgProduto);
        textProduto.setText(produto.getNome());
        textDescricao.setText(produto.getDescricao());
        textValor.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        if (produto.getValorAntigo() > 0) {
            textValorAntigo.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValorAntigo())));
        } else {
            textValorAntigo.setText("");
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
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