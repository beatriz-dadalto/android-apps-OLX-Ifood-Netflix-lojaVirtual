package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.SelecionaEnderecoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioSelecionaEnderecoActivity extends AppCompatActivity implements SelecionaEnderecoAdapter.OnClickListener {

    private SelecionaEnderecoAdapter selecionaEnderecoAdapter;
    private List<Endereco> enderecoList = new ArrayList<>();

    private RecyclerView rvEnderecos;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_seleciona_endereco);

        inciarComponentes();
        configCliques();
        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarEnderecos();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_add).setOnClickListener(view ->
                startActivity(new Intent(this, UsuarioFormEnderecoActivity.class)));
    }

    private void inciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Meus Endereços");

        rvEnderecos = findViewById(R.id.rv_enderecos);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
    }

    private void configRv() {
        rvEnderecos.setLayoutManager(new LinearLayoutManager(this));
        rvEnderecos.setHasFixedSize(true);
        selecionaEnderecoAdapter = new SelecionaEnderecoAdapter(enderecoList, this);
        rvEnderecos.setAdapter(selecionaEnderecoAdapter);
    }

    private void recuperarEnderecos() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    enderecoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Endereco endereco = ds.getValue(Endereco.class);
                        enderecoList.add(endereco);
                    }
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum endereço cadastrado");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(enderecoList);
                selecionaEnderecoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(Endereco endereco) {
        Intent intent = new Intent();
        intent.putExtra("enderecoSelecionado", endereco);
        setResult(RESULT_OK, intent);
        finish();
    }
}