package com.br.bancodigital.transferencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.bancodigital.R;
import com.br.bancodigital.adapter.UsuarioAdapter;
import com.br.bancodigital.helper.FirebaseHelper;
import com.br.bancodigital.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransferenciaUsuarioActivity extends AppCompatActivity {

    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> usuarioList = new ArrayList<>();

    private RecyclerView rvUsuarios;
    private EditText edtPesquisa;
    private TextView textInfo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia_usuario);

        configToolbar();
        iniciaComponentes();
        recuperarUsuarios();
        configRv();
    }

    private void recuperarUsuarios() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios");
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usuarioList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Usuario usuario = ds.getValue(Usuario.class);

                        if (usuario != null) {
                            if (!usuario.getId().equals(FirebaseHelper.getIdFirebase())) {
                                usuarioList.add(usuario);
                            } else {
                                textInfo.setText("\uD83D\uDC64 \nNenhum usuário cadastrado!");
                            }
                        } else {
                            textInfo.setText("");
                        }
                    }
                } else {
                    textInfo.setText("\uD83D\uDC64 \nNenhum usuário cadastrado!");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(usuarioList);
                usuarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        rvUsuarios.setHasFixedSize(true);
        usuarioAdapter = new UsuarioAdapter(usuarioList);
        rvUsuarios.setAdapter(usuarioAdapter);
    }

    private void configToolbar() {
        TextView textTitulo = findViewById(R.id.textTitulo);
        textTitulo.setText("Selecione o usuário");

        findViewById(R.id.ibVoltar).setOnClickListener(v -> finish());
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void iniciaComponentes() {
        rvUsuarios = findViewById(R.id.rvUsuarios);
        edtPesquisa = findViewById(R.id.edtPesquisa);
        textInfo = findViewById(R.id.textInfo);
        progressBar = findViewById(R.id.progressBar);
    }
}