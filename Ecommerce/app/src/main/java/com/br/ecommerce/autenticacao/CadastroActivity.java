package com.br.ecommerce.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityCadastroBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Loja;
import com.br.ecommerce.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.include.ibVoltar.setOnClickListener(view -> finish());
        binding.btnLogin.setOnClickListener(view -> finish());
    }

    public void validaDados(View view) {
        String nome = binding.edtNome.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String senha = binding.edtSenha.getText().toString().trim();
        String confirmaSenha = binding.edtConfirmaSenha.getText().toString().trim();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    if (!confirmaSenha.isEmpty()) {
                        if (confirmaSenha.equals(senha)) {

                            ocultarTeclado();
                            binding.progressBar.setVisibility(View.VISIBLE);


                               // USAR PARA CRIAR UMA LOJA
                                Loja loja = new Loja();
                                loja.setNome(nome);
                                loja.setEmail(email);
                                loja.setSenha(senha);

                                criarLoja(loja);


//                            Usuario usuario = new Usuario();
//                            usuario.setNome(nome);
//                            usuario.setEmail(email);
//                            usuario.setSenha(senha);
//
//                            criarConta(usuario);

                        } else {
                            binding.edtConfirmaSenha.requestFocus();
                            binding.edtConfirmaSenha.setError("Senhas diferentes.");
                        }
                    } else {
                        binding.edtConfirmaSenha.requestFocus();
                        binding.edtConfirmaSenha.setError("Confirme a senha.");
                    }
                } else {
                    binding.edtSenha.requestFocus();
                    binding.edtSenha.setError("Informe uma senha.");
                }
            } else {
                binding.edtEmail.requestFocus();
                binding.edtEmail.setError("Informe o email.");
            }
        } else {
            binding.edtNome.requestFocus();
            binding.edtNome.setError("Informe o nome.");
        }
    }

    private void criarLoja(Loja loja) {
        FirebaseHelper.getAuth()
                .createUserWithEmailAndPassword(loja.getEmail(), loja.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String id = task.getResult().getUser().getUid();
                        loja.setId(id);
                        loja.salvar();

                    } else {
                        Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_LONG).show();
                    }

                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private void criarConta(Usuario usuario) {
        FirebaseHelper.getAuth()
                .createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String id = task.getResult().getUser().getUid();
                        usuario.setId(id);
                        usuario.salvar();

                        Intent intent = new Intent();
                        intent.putExtra("email", usuario.getEmail());
                        setResult(RESULT_OK, intent);

                        finish();
                    } else {
                        Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_LONG).show();
                    }

                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private void ocultarTeclado() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}