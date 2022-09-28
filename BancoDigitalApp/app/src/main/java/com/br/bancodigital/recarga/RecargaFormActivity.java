package com.br.bancodigital.recarga;

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
import com.br.bancodigital.deposito.DepositoReciboActivity;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Deposito;
import com.br.bancodigital.model.Extrato;
import com.br.bancodigital.model.Recarga;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Locale;

public class RecargaFormActivity extends AppCompatActivity {

    private CurrencyEditText edtValor;
    private MaskEditText edtTelefone;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_form);

        iniciaComponentes();
        configToolbar();
        recuperaUsuario();
    }

    public void validaDados(View view) {
        double valor = (double) edtValor.getRawValue() / 100;
        String numero = edtTelefone.getUnMasked().trim();

        if (valor >= 15) {
            if (valor <= usuario.getSaldo()) {
                if (!numero.isEmpty()) {
                    if (numero.length() == 11) {

                        progressBar.setVisibility(View.VISIBLE);

                        salvarExtrato(valor, numero);

                    } else {
                        showDialog("O número digitado está incompleto");
                    }
                } else {
                    edtTelefone.requestFocus();
                    showDialog("Informe o número do celular");
                }
            } else {
                showDialog("Saldo insuficiente! \uD83E\uDD72");
            }
        } else {
            showDialog("Recarga mínima de R$ 15,00.");
        }
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void salvarExtrato(double valor, String numero) {

        Extrato extrato = new Extrato();
        extrato.setOperacao("RECARGA");
        extrato.setValor(valor);
        extrato.setTipo("SAIDA");

        DatabaseReference extratoRef = FirebaseHelper.getDatabaseReference()
                .child("extratos")
                .child(FirebaseHelper.getIdFirebase())
                .child(extrato.getId());
        extratoRef.setValue(extrato).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateExtrato = extratoRef
                        .child("data");
                updateExtrato.setValue(ServerValue.TIMESTAMP);

                salvarRecarga(extrato, numero);

            } else {
                showDialog("Não foi possível efetuar o deposito, tente mais tarde.");
            }
        });

    }

    private void salvarRecarga(Extrato extrato, String numero) {

        Recarga recarga = new Recarga();
        recarga.setId(extrato.getId());
        recarga.setNumero(numero);
        recarga.setValor(extrato.getValor());

        DatabaseReference recargaRef = FirebaseHelper.getDatabaseReference()
                .child("recargas")
                .child(recarga.getId());

        recargaRef.setValue(recarga).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DatabaseReference updateRecarga = recargaRef
                        .child("data");
                updateRecarga.setValue(ServerValue.TIMESTAMP);

                usuario.setSaldo(usuario.getSaldo() - recarga.getValor());
                usuario.atualizarSaldo();

                Intent intent = new Intent(this, RecargaReciboActivity.class);
                intent.putExtra("idRecarga", recarga.getId());
                startActivity(intent);
                finish();

            } else {
                progressBar.setVisibility(View.GONE);
                showDialog("Não foi possível efetuar o recarga, tente mais tarde.");
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