package com.biamailov3.ifoodclone.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.model.Login;
import com.biamailov3.ifoodclone.model.Usuario;
import com.santalu.maskara.widget.MaskEditText;

public class UsuarioFinalizaCadastroActivity extends AppCompatActivity {

    private EditText edtNome;
    private MaskEditText edtTelefone;
    private ProgressBar progressBar;

    private Usuario usuario;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_finaliza_cadastro);

        // recuperar user e login para finalizar cadastro
        // os dados virão da UsuarioFragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuario = (Usuario) bundle.getSerializable("usuario");
            login = (Login) bundle.getSerializable("login");
        }

        iniciarComponentes();
        configCliques();
    }

    public void validarDados(View view) {
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getUnMasked();

        if (!nome.isEmpty()) {
            if (!telefone.isEmpty()) {
                if (edtTelefone.isDone()) {

                    ocultarTeclado();
                    progressBar.setVisibility(View.VISIBLE);
                    finalizarCadastro(nome); // os dados do user foram recuperados com o bundle

                } else {
                    edtTelefone.requestFocus();
                    edtTelefone.setError("Telefone inválido");
                }
            } else {
                edtTelefone.requestFocus();
                edtTelefone.setError("Informe seu telefone");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Informe seu nome");
        }
    }

    private void finalizarCadastro(String nome) {
        login.setAcesso(true);
        login.salvar();

        usuario.setNome(nome);
        usuario.salvar();

        finish();
        startActivity(new Intent(this, UsuarioHomeActivity.class));
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtNome.getWindowToken(), 0);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Finalizar Cadastro");

        edtNome = findViewById(R.id.edt_nome);
        edtTelefone = findViewById(R.id.edt_telefone);
        progressBar = findViewById(R.id.progressBar);
    }

}