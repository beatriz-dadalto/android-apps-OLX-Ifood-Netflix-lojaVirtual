package com.br.ecommerce.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityRecuperaContaBinding;
import com.br.ecommerce.helper.FirebaseHelper;

public class RecuperaContaActivity extends AppCompatActivity {

    private ActivityRecuperaContaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperaContaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.include.ibVoltar.setOnClickListener(view -> finish());
    }

    public void validaDados(View view) {
        String email = binding.edtEmail.getText().toString().trim();

        if (!email.isEmpty()) {

            binding.progressBar.setVisibility(View.VISIBLE);
            recuperaConta(email);

        } else {
            binding.edtEmail.requestFocus();
            binding.edtEmail.setError("Informe seu email");
        }
    }

    private void recuperaConta(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Verifique seu email. Enviamos um link para atualizar sua senha.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_LONG).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                });
    }
}