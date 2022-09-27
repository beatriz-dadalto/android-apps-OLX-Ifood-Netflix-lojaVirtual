package com.br.bancodigital.recarga;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.br.bancodigital.R;
import com.br.bancodigital.deposito.DepositoReciboActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Deposito;
import com.br.bancodigital.model.Extrato;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.Locale;

public class RecargaFormActivity extends AppCompatActivity {

    private CurrencyEditText edtValor;
    private EditText edtTelefone;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_form);

        iniciaComponentes();
        configToolbar();
    }

    public void validaDados(View view) {
        double valor = (double) edtValor.getRawValue() / 100;
        String numero = edtTelefone.getText().toString().trim();

        if (valor >= 15) {
            if (!numero.isEmpty()) {
                if (numero.length() == 11) {

                    progressBar.setVisibility(View.VISIBLE);

                    Toast.makeText(this, "Tudo certo!", Toast.LENGTH_SHORT).show();

                } else {
                    showDialog("O número digitado está incompleto");
                }
            } else {
                edtTelefone.requestFocus();
                showDialog("Informe o número");
            }
        } else {
            showDialog("Recarga mínima de R$ 15,00.");
        }
    }

    private void salvarExtrato(double valorDeposito) {

        Extrato extrato = new Extrato();
        extrato.setOperacao("DEPOSITO");
        extrato.setValor(valorDeposito);
        extrato.setTipo("ENTRADA");

        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference()
                .child("extratos")
                .child(FirebaseHelper.getIdFirebase())
                .child(extrato.getId());
        extratoRef.setValue(extrato).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateExtrato = extratoRef
                        .child("data");
                updateExtrato.setValue(ServerValue.TIMESTAMP);

                salvarDeposito(extrato);

            } else {
                showDialog("Não foi possível efetuar o deposito, tente mais tarde.");
            }
        });

    }

    private void salvarDeposito(Extrato extrato) {

        Deposito deposito = new Deposito();
        deposito.setId(extrato.getId());
        deposito.setValor(extrato.getValor());

        DatabaseReference depositoRef = FirebaseHelper.getDatabaseReference()
                .child("depositos")
                .child(deposito.getId());

        depositoRef.setValue(deposito).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateDeposito = depositoRef
                        .child("data");
                updateDeposito.setValue(ServerValue.TIMESTAMP);

                usuario.setSaldo(usuario.getSaldo() + deposito.getValor());
                usuario.atualizarSaldo();

                Intent intent = new Intent(this, DepositoReciboActivity.class);
                intent.putExtra("idDeposito", deposito.getId());
                startActivity(intent);
                finish();

            } else {
                progressBar.setVisibility(View.GONE);
                showDialog("Não foi possível efetuar o deposito, tente mais tarde.");
            }
        });

    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this, R.style.CustomAlertDialog
        );

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Recarga");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void iniciaComponentes() {
        edtValor = findViewById(R.id.edtValor);
        edtValor.setLocale(new Locale("PT", "br"));
        edtTelefone = findViewById(R.id.edtTelefone);

        progressBar = findViewById(R.id.progressBar);
    }
}