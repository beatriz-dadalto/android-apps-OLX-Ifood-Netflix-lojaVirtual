package com.biamailov3.ifoodclone.activity.empresa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.Locale;

public class EmpresaFormProdutoActivity extends AppCompatActivity {

    private ImageView imgProduto;
    private EditText edtProduto;
    private CurrencyEditText edtValor;
    private CurrencyEditText edtValorAntigo;
    private Button btnCategoria;
    private EditText edtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_form_produto);

        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Novo produto");

        imgProduto = findViewById(R.id.img_produto);
        edtProduto = findViewById(R.id.edt_produto);
        edtValor = findViewById(R.id.edt_valor);
        edtValor.setLocale(new Locale("PT", "br"));
        edtValorAntigo = findViewById(R.id.edt_valor_antigo);
        edtValorAntigo.setLocale(new Locale("PT", "br"));
        btnCategoria = findViewById(R.id.btn_categoria);
        edtDescricao = findViewById(R.id.edt_descricao);
    }
}