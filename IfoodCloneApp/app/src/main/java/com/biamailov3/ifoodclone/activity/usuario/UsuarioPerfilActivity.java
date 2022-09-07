package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

public class UsuarioPerfilActivity extends AppCompatActivity {

    private EditText edtNome;
    private MaskEditText edtTelefone;
    private EditText edtEmail;
    private ProgressBar progressBar;
    private ImageButton ibSalvar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        inciarComponentes();
        configCliques();
        recuperarUsuario();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_salvar).setOnClickListener(view -> validarDados());
    }

    private void recuperarUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usuario = snapshot.getValue(Usuario.class);

                    configDados();
                } else {
                    configSalvar(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        edtNome.setText(usuario.getNome());
        edtTelefone.setText(usuario.getTelefone());
        edtEmail.setText(usuario.getEmail());

        configSalvar(false);
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

    private void validarDados() {
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getUnMasked();

        if (!nome.isEmpty()) {
            if (edtTelefone.isDone()) {

                configSalvar(true);
                ocultarTeclado();

                if (usuario == null) usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setTelefone(telefone);
                usuario.salvar();

                configSalvar(false);

            } else {
                edtTelefone.requestFocus();
                edtTelefone.setError("Informe um telefone");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Informe o nome da empresa");
        }
    }

    private void inciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Perfil");

        edtNome = findViewById(R.id.edt_nome);
        edtTelefone = findViewById(R.id.edt_telefone);
        edtEmail = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
        ibSalvar = findViewById(R.id.ib_salvar);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtNome.getWindowToken(), 0);
    }
}