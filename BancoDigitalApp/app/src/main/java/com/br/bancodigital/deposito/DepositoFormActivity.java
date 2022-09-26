package com.br.bancodigital.deposito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.br.bancodigital.R;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Deposito;
import com.br.bancodigital.model.Extrato;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class DepositoFormActivity extends AppCompatActivity {

    private CurrencyEditText edtValor;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposito_form);

        configToolbar();
        recuperaUsuario();
        iniciaComponentes();
    }

    public void validaDeposito(View view) {
        double valorDepositado = (double) edtValor.getRawValue() / 100;

        if (valorDepositado > 0) {
            ocultarTeclado();
            progressBar.setVisibility(View.VISIBLE);

            salvarExtrato(valorDepositado);
        } else {
            showDialog("Digite um valor maior que zero.");
        }
    }

    private void salvarExtrato(double valorDepositado) {
        Extrato extrato = new Extrato();
        extrato.setOperacao("DEPOSITO");
        extrato.setValor(valorDepositado);
        extrato.setTipo("ENTRADA");

        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference()
                .child("extratos")
                .child(FirebaseHelper.getIdFirebase())
                .child(extrato.getId());
        extratoRef.setValue(extrato).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DatabaseReference updateRef = extratoRef
                        .child("data");
                updateRef.setValue(ServerValue.TIMESTAMP);

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

                //startActivity(new Intent(this, DepositoReciboActivity.class));
            } else {
                progressBar.setVisibility(View.GONE);
                showDialog("Não foi possível efetuar o deposito, tente mais tarde.");
            }
        });
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

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