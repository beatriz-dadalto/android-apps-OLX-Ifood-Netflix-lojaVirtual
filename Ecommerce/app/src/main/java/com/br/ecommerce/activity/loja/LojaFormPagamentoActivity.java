package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityLojaFormPagamentoBinding;
import com.br.ecommerce.model.FormaPagamento;
import com.br.ecommerce.model.TipoValor;

import java.util.Locale;

public class LojaFormPagamentoActivity extends AppCompatActivity {

    private ActivityLojaFormPagamentoBinding binding;
    private FormaPagamento formaPagamento;
    private TipoValor tipoValor = null;
    private boolean novoPagamento = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaFormPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
        getExtra();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            formaPagamento = (FormaPagamento) bundle.getSerializable("formaPagamentoSelecionada");
            configDados();
        }
    }

    private void configDados() {
        // false quer dizer que eh uma edicao
        novoPagamento = false;

        binding.edtFormaPagamento.setText(formaPagamento.getNome());
        binding.edtDescricaoPagamento.setText(formaPagamento.getDescricao());
        binding.edtValor.setText(String.valueOf(formaPagamento.getValor() * 10));

        if (formaPagamento.getTipoValor().equals(TipoValor.DESCONTO)) {
            binding.rgValor.check(R.id.rbDesconto);
        } else if (formaPagamento.getTipoValor().equals(TipoValor.ACRESCIMO)){
            binding.rgValor.check(R.id.rbAcrescimo);
        }

        binding.cbCredito.setChecked(formaPagamento.isCredito());
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
        binding.include.btnSalvar.setOnClickListener(view -> validaDados());

        binding.rgValor.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbDesconto) {
                tipoValor = TipoValor.DESCONTO;
            } else if (i == R.id.rbAcrescimo){
                tipoValor = TipoValor.ACRESCIMO;
            }
        });
    }

    private void iniciaComponentes() {
        binding.edtValor.setLocale(new Locale("PT", "br"));
        binding.include.textTitulo.setText("Forma de pagamento");
    }

    private void validaDados() {
        String nome = binding.edtFormaPagamento.getText().toString().trim();
        String descricao = binding.edtDescricaoPagamento.getText().toString().trim();
        double valor = (double) binding.edtValor.getRawValue() / 100;

        if(!nome.isEmpty()) {
            if(!descricao.isEmpty()) {
                ocultaTeclado();

                binding.progressBar.setVisibility(View.VISIBLE);

                if (formaPagamento == null) formaPagamento = new FormaPagamento();
                formaPagamento.setNome(nome);
                formaPagamento.setDescricao(descricao);
                formaPagamento.setValor(valor);
                formaPagamento.setTipoValor(tipoValor);
                formaPagamento.setCredito(binding.cbCredito.isChecked());

                if (formaPagamento.getTipoValor() != null) {
                    formaPagamento.salvar();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Selecione o tipo do valor", Toast.LENGTH_SHORT).show();
                }
                
                if (novoPagamento) {
                    Intent intent = new Intent();
                    intent.putExtra("novoPagamento", formaPagamento);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Forma de pagamento foi salva!", Toast.LENGTH_SHORT).show();
                }
            } else {
                binding.edtDescricaoPagamento.requestFocus();
                binding.edtDescricaoPagamento.setError("Informação obrigatória");
            }
        } else {
            binding.edtFormaPagamento.requestFocus();
            binding.edtFormaPagamento.setError("Informação obrigatória");
        }
    }
    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtValor.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}