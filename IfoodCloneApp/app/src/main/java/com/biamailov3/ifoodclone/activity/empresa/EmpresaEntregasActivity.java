package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Entrega;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EmpresaEntregasActivity extends AppCompatActivity {

    private List<Entrega> entregaList = new ArrayList<>();

    private Entrega domicilio = new Entrega();
    private Entrega retirada = new Entrega();
    private Entrega outra =new Entrega();

    private CheckBox cbDomicilio;
    private CheckBox cbRetirada;
    private CheckBox cbOutra;
    private CurrencyEditText edtDomicilio;
    private CurrencyEditText edtRetirada;
    private CurrencyEditText edtOutra;

    private ImageButton ibSalvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_entregas);

        iniciarComponentes();
        configCliques();
        recuperarEntregas();
    }

    private void configCliques() {
        ibSalvar.setOnClickListener(view -> validarEntregas());
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
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

    private void recuperarEntregas() {
        DatabaseReference entregasRef = FirebaseHelper.getDatabaseReference()
                .child("entregas")
                .child(FirebaseHelper.getIdFirebase());
        entregasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    entregaList.clear(); // evitar dados duplicados
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Entrega entrega = ds.getValue(Entrega.class);
                        configEntregas(entrega);
                    }
                } else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configEntregas(Entrega entrega) {
        switch (entrega.getDescricao()) {
            case "Domicílio":
                domicilio = entrega;
                edtDomicilio.setText(String.valueOf(domicilio.getTaxa() * 10));
                cbDomicilio.setChecked(domicilio.getStatus());
                break;
            case "Retirada":
                retirada = entrega;
                edtRetirada.setText(String.valueOf(retirada.getTaxa() * 10));
                cbRetirada.setChecked(retirada.getStatus());
                break;
            case "Outra":
                outra = entrega;
                edtOutra.setText(String.valueOf(outra.getTaxa() * 10));
                cbOutra.setChecked(outra.getStatus());
                break;
        }
        configSalvar(false);
    }

    private void validarEntregas() {
        entregaList.clear();
        configSalvar(true);

        domicilio.setStatus(cbDomicilio.isChecked());
        domicilio.setTaxa((double) edtDomicilio.getRawValue() / 100);
        domicilio.setDescricao("Domicílio");

        retirada.setStatus(cbRetirada.isChecked());
        retirada.setTaxa((double) edtRetirada.getRawValue() / 100);
        retirada.setDescricao("Retirada");

        outra.setStatus(cbOutra.isChecked());
        outra.setTaxa((double) edtOutra.getRawValue() / 100);
        outra.setDescricao("Outra");

        entregaList.add(domicilio);
        entregaList.add(retirada);
        entregaList.add(outra);

        Entrega.salvar(entregaList);
        configSalvar(false);
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Entregas");

        cbDomicilio = findViewById(R.id.cb_domicilio);
        cbRetirada = findViewById(R.id.cb_retirada);
        cbOutra = findViewById(R.id.cb_outra);

        edtDomicilio = findViewById(R.id.edt_domicilio);
        edtDomicilio.setLocale(new Locale("PT", "br"));

        edtRetirada = findViewById(R.id.edt_retirada);
        edtDomicilio.setLocale(new Locale("PT", "br"));

        edtOutra = findViewById(R.id.edt_outra);
        edtDomicilio.setLocale(new Locale("PT", "br"));

        ibSalvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);
    }
}