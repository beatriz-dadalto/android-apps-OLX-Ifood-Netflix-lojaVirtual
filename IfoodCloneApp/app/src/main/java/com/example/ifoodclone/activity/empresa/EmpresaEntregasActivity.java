package com.example.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Entrega;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EmpresaEntregasActivity extends AppCompatActivity {

    private final List<Entrega> entregaList = new ArrayList<>();

    private Entrega domicilio = new Entrega();
    private Entrega retirada = new Entrega();
    private Entrega outra = new Entrega();

    private CheckBox cb_domicilio;
    private CheckBox cb_retirada;
    private CheckBox cb_outra;

    private CurrencyEditText edt_domicilio;
    private CurrencyEditText edt_retirada;
    private CurrencyEditText edt_outra;

    private ImageButton ib_salvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_entregas);

        iniciaComponentes();

        configCliques();

        recuperaEntregas();
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

    private void recuperaEntregas(){
        DatabaseReference entregasRef = FirebaseHelper.getDatabaseReference()
                .child("entregas")
                .child(FirebaseHelper.getIdFirebase());
        entregasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    entregaList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Entrega entrega = ds.getValue(Entrega.class);
                        configEntregas(entrega);
                    }
                }else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques(){
        ib_salvar.setOnClickListener(v -> validaEntregas());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configEntregas(Entrega entrega){
        switch (entrega.getDescricao()){
            case "Domicílio":
                domicilio = entrega;
                edt_domicilio.setText(String.valueOf(domicilio.getTaxa() * 10));
                cb_domicilio.setChecked(domicilio.getStatus());
            break;
            case "Retirada":
                retirada = entrega;
                edt_retirada.setText(String.valueOf(retirada.getTaxa() * 10));
                cb_retirada.setChecked(retirada.getStatus());
                break;
            case "Outra":
                outra = entrega;
                edt_outra.setText(String.valueOf(outra.getTaxa() * 10));
                cb_outra.setChecked(outra.getStatus());
                break;
        }

        configSalvar(false);
    }

    private void validaEntregas(){

        entregaList.clear();
        configSalvar(true);

        domicilio.setStatus(cb_domicilio.isChecked());
        domicilio.setTaxa((double) edt_domicilio.getRawValue() / 100);
        domicilio.setDescricao("Domicílio");

        retirada.setStatus(cb_retirada.isChecked());
        retirada.setTaxa((double) edt_retirada.getRawValue() / 100);
        retirada.setDescricao("Retirada");

        outra.setStatus(cb_outra.isChecked());
        outra.setTaxa((double) edt_outra.getRawValue() / 100);
        outra.setDescricao("Outra");

        entregaList.add(domicilio);
        entregaList.add(retirada);
        entregaList.add(outra);

        Entrega.salvar(entregaList);
        configSalvar(false);
        finish();
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Entregas");

        cb_domicilio = findViewById(R.id.cb_domicilio);
        cb_retirada = findViewById(R.id.cb_retirada);
        cb_outra = findViewById(R.id.cb_outra);

        edt_domicilio = findViewById(R.id.edt_domicilio);
        edt_domicilio.setLocale(new Locale("PT", "br"));

        edt_retirada = findViewById(R.id.edt_retirada);
        edt_retirada.setLocale(new Locale("PT", "br"));

        edt_outra = findViewById(R.id.edt_outra);
        edt_outra.setLocale(new Locale("PT", "br"));

        ib_salvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }

}