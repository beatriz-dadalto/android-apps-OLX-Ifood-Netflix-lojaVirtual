package com.example.ifoodclone.activity.empresa;

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

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Pagamento;
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

    private CheckBox cb_de;
    private CheckBox cb_dr;
    private CheckBox cb_cce;
    private CheckBox cb_ccr;
    private CheckBox cb_app;

    private EditText edt_public_key;
    private EditText edt_access_token;

    private ImageButton ib_salvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_recebimentos);

        iniciaComponentes();

        configCliques();

        recuperaPagamentos();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        ib_salvar.setOnClickListener(v -> salvarPagamentos());

        // Dinheiro na entrega
        cb_de.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiro.setDescricao("Dinheiro na entrega");
            dinheiro.setStatus(isChecked);
        });

        // Dinheiro na retirada
        cb_dr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiroEntrega.setDescricao("Dinheiro na retirada");
            dinheiroEntrega.setStatus(isChecked);
        });

        // Cartão de crédito na entrega
        cb_cce.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoEntrega.setDescricao("Cartão de crédito na entrega");
            cartaoCreditoEntrega.setStatus(isChecked);
        });

        // Cartão de crédito na retirada
        cb_ccr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoRetirada.setDescricao("Cartão de crédito na retirada");
            cartaoCreditoRetirada.setStatus(isChecked);
        });

        // Cartão de crédito pelo app
        cb_app.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoApp.setDescricao("Cartão de crédito pelo app");
            cartaoCreditoApp.setStatus(isChecked);
        });
    }

    private void salvarPagamentos() {

        if(cb_de.isChecked()){
            if(!pagamentoList.contains(dinheiro)) pagamentoList.add(dinheiro);
        }

        if(cb_dr.isChecked()){
            if(!pagamentoList.contains(dinheiroEntrega)) pagamentoList.add(dinheiroEntrega);
        }

        if(cb_cce.isChecked()){
            if(!pagamentoList.contains(cartaoCreditoEntrega)) pagamentoList.add(cartaoCreditoEntrega);
        }

        if(cb_ccr.isChecked()){
            if(!pagamentoList.contains(cartaoCreditoRetirada)) pagamentoList.add(cartaoCreditoRetirada);
        }

        if(cb_app.isChecked()){
            if(!pagamentoList.contains(cartaoCreditoApp)) pagamentoList.add(cartaoCreditoApp);
        }

        Pagamento.salvar(pagamentoList);
        finish();
    }

    private void recuperaPagamentos(){
        DatabaseReference pagamentosRef = FirebaseHelper.getDatabaseReference()
                .child("recebimentos")
                .child(FirebaseHelper.getIdFirebase());
        pagamentosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Pagamento pagamento = ds.getValue(Pagamento.class);
                        pagamentoList.add(pagamento);
                    }

                    configPagamentos();

                }else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configSalvar(boolean progress){
        if(progress){
            progressBar.setVisibility(View.VISIBLE);
            ib_salvar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            ib_salvar.setVisibility(View.VISIBLE);
        }
    }

    private void configPagamentos() {
        for (Pagamento pagamento : pagamentoList) {
            switch (pagamento.getDescricao()){
                case "Dinheiro na entrega":
                    dinheiro = pagamento;
                    cb_de.setChecked(dinheiro.getStatus());
                    break;
                case "Dinheiro na retirada":
                    dinheiroEntrega = pagamento;
                    cb_dr.setChecked(dinheiroEntrega.getStatus());
                    break;
                case "Cartão de crédito na entrega":
                    cartaoCreditoEntrega = pagamento;
                    cb_cce.setChecked(cartaoCreditoEntrega.getStatus());
                    break;
                case "Cartão de crédito na retirada":
                    cartaoCreditoRetirada = pagamento;
                    cb_ccr.setChecked(cartaoCreditoRetirada.getStatus());
                    break;
                case "Cartão de crédito pelo app":
                    cartaoCreditoApp = pagamento;
                    cb_app.setChecked(cartaoCreditoApp.getStatus());
                    break;
            }
        }
        configSalvar(false);
    }

    private void validaPagamentos(){

    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Recebimentos");

        cb_de = findViewById(R.id.cb_de);
        cb_dr = findViewById(R.id.cb_dr);
        cb_cce = findViewById(R.id.cb_cce);
        cb_ccr = findViewById(R.id.cb_ccr);
        cb_app = findViewById(R.id.cb_app);

        edt_public_key = findViewById(R.id.edt_public_key);
        edt_access_token = findViewById(R.id.edt_access_token);

        ib_salvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }

}