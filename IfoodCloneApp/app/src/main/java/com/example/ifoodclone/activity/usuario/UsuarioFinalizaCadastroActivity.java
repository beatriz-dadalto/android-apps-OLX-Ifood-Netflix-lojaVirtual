package com.example.ifoodclone.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Login;
import com.example.ifoodclone.model.Usuario;
import com.santalu.maskara.widget.MaskEditText;

public class UsuarioFinalizaCadastroActivity extends AppCompatActivity {

    private EditText edt_nome;
    private MaskEditText edt_telefone;
    private ProgressBar progressBar;

    private Usuario usuario;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_finaliza_cadastro);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuario = (Usuario) bundle.getSerializable("usuario");
            login = (Login) bundle.getSerializable("login");
        }

        iniciaComponentes();

    }

    public void validaDados(View view){
        String nome = edt_nome.getText().toString().trim();
        String telefone = edt_telefone.getUnMasked();

        if(!nome.isEmpty()){
            if(!telefone.isEmpty()){
                if(edt_telefone.isDone()){

                    ocultarTeclado();

                    progressBar.setVisibility(View.VISIBLE);

                    finalizaCadastro(nome, telefone);

                }else {
                    edt_telefone.requestFocus();
                    edt_telefone.setError("Telefone inválido.");
                }
            }else {
                edt_telefone.requestFocus();
                edt_telefone.setError("Informe seu telefone.");
            }
        }else {
            edt_nome.requestFocus();
            edt_nome.setError("Informe seu nome.");
        }

    }

    private void finalizaCadastro(String nome, String telefone){
        login.setAcesso(true);
        login.salvar();

        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.salvar();

        finish();
        startActivity(new Intent(this, UsuarioHomeActivity.class));
    }

    private void iniciaComponentes(){
        edt_nome = findViewById(R.id.edt_nome);
        edt_telefone = findViewById(R.id.edt_telefone);
        progressBar = findViewById(R.id.progressBar);
    }

    private void ocultarTeclado(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                edt_nome.getWindowToken(), 0
        );
    }

}