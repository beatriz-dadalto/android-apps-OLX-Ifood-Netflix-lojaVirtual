package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Pagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresaRecebimentosActivity extends AppCompatActivity {

    private List<Pagamento> pagamentoList = new ArrayList<>();

    private Pagamento dinheiro = new Pagamento();
    private Pagamento dinheiroEntrega = new Pagamento();
    private Pagamento cartaoCreditoEntrega = new Pagamento();
    private Pagamento cartaoCreditoRetirada = new Pagamento();
    private Pagamento cartaoCreditoApp = new Pagamento();

    private CheckBox cbDinheiroNaEntrega;
    private CheckBox cbDinheiroNaRetirada;
    private CheckBox cbCartaoCreditoNaEntrega;
    private CheckBox cbCartaoCreditoNaRetirada;
    private CheckBox cbPagamentoPeloApp;

    private EditText edtPublicKey;
    private EditText edtAccessToken;

    private ImageButton ibSalvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_recebimentos);

        iniciarComponentes();
        configCliques();
        recuperarPagamentos();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        ibSalvar.setOnClickListener(view -> salvarPagamentos());

        cbDinheiroNaEntrega.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiro.setDescricao("Dinheiro na entrega");
            dinheiro.setStatus(isChecked);
        });

        cbDinheiroNaRetirada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiroEntrega.setDescricao("Dinheiro na retirada");
            dinheiroEntrega.setStatus(isChecked);
        });

        cbCartaoCreditoNaEntrega.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoEntrega.setDescricao("Cartão de crédito na entrega");
            cartaoCreditoEntrega.setStatus(isChecked);
        });

        cbCartaoCreditoNaRetirada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoRetirada.setDescricao("Cartão de crédito na retirada");
            cartaoCreditoRetirada.setStatus(isChecked);
        });

        cbPagamentoPeloApp.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoApp.setDescricao("Cartão de crédito pelo app");
            cartaoCreditoApp.setStatus(isChecked);
        });
    }

    private void salvarPagamentos() {

        if (cbDinheiroNaEntrega.isChecked()) {
            if (!pagamentoList.contains(dinheiro)) pagamentoList.add(dinheiro);
        }

        if (cbDinheiroNaRetirada.isChecked()) {
            if (!pagamentoList.contains(dinheiroEntrega)) pagamentoList.add(dinheiroEntrega);
        }

        if (cbCartaoCreditoNaEntrega.isChecked()) {
            if (!pagamentoList.contains(cartaoCreditoEntrega)) pagamentoList.add(cartaoCreditoEntrega);
        }

        if (cbCartaoCreditoNaRetirada.isChecked()) {
            if (!pagamentoList.contains(cartaoCreditoRetirada)) pagamentoList.add(cartaoCreditoRetirada);
        }

        if (cbPagamentoPeloApp.isChecked()) {
            if (!pagamentoList.contains(cartaoCreditoApp)) pagamentoList.add(cartaoCreditoApp);
        }

        Pagamento.salvar(pagamentoList);
    }

    private void recuperarPagamentos() {
        DatabaseReference pagamentosRef = FirebaseHelper.getDatabaseReference()
                .child("recebimentos")
                .child(FirebaseHelper.getIdFirebase());
        pagamentosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pagamento pagamento = ds.getValue(Pagamento.class);
                        pagamentoList.add(pagamento);
                    }

                    configPagamentos();
                } else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configSalvar(boolean showProgressBar) {
        if (showProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
            ibSalvar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            ibSalvar.setVisibility(View.VISIBLE);
        }
    }

    private void configPagamentos() {
        for (Pagamento pagamento : pagamentoList) {
            switch (pagamento.getDescricao()) {
                case "Dinheiro na entrega":
                    dinheiro = pagamento;
                    cbDinheiroNaEntrega.setChecked(dinheiro.getStatus());
                    break;
                case "Dinheiro na retirada":
                    dinheiroEntrega = pagamento;
                    cbDinheiroNaRetirada.setChecked(dinheiroEntrega.getStatus());
                    break;
                case "Cartão de crédito na entrega":
                    cartaoCreditoEntrega = pagamento;
                    cbCartaoCreditoNaEntrega.setChecked(cartaoCreditoEntrega.getStatus());
                    break;
                case "Cartão de crédito na retirada":
                    cartaoCreditoRetirada = pagamento;
                    cbCartaoCreditoNaRetirada.setChecked(cartaoCreditoRetirada.getStatus());
                    break;
                case "Cartão de crédito pelo app":
                    cartaoCreditoApp = pagamento;
                    cbPagamentoPeloApp.setChecked(cartaoCreditoApp.getStatus());
                    break;
            }
        }
        configSalvar(false);
    }

    private void validarPagamentos() {

    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Recebimentos");

        cbDinheiroNaEntrega = findViewById(R.id.cb_de);
        cbDinheiroNaRetirada = findViewById(R.id.cb_dr);
        cbCartaoCreditoNaEntrega = findViewById(R.id.cb_cce);
        cbCartaoCreditoNaRetirada = findViewById(R.id.cb_ccr);
        cbPagamentoPeloApp = findViewById(R.id.cb_app);

        edtPublicKey = findViewById(R.id.edt_public_key);
        edtAccessToken = findViewById(R.id.edt_access_token);

        ibSalvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }
}