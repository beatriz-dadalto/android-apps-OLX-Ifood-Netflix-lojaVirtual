package com.br.bancodigital.deposito;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.br.bancodigital.R;

import java.util.Locale;

public class DepositoFormActivity extends AppCompatActivity {

    private CurrencyEditText edtValor;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposito_form);

        configToolbar();
        iniciaComponentes();
    }

    public void validaDeposito(View view) {
        long valorDepositado = edtValor.getRawValue() / 100;

        if (valorDepositado > 0) {
            ocultarTeclado();
            progressBar.setVisibility(View.VISIBLE);

            // TODO salvar extrato
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText("Digite um valor maior que zero");

        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        // exibir a view
        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void iniciaComponentes() {
        edtValor = findViewById(R.id.edtValor);
        edtValor.setLocale(new Locale("PT", "br"));
        progressBar = findViewById(R.id.progressBar);
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Depositar");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}