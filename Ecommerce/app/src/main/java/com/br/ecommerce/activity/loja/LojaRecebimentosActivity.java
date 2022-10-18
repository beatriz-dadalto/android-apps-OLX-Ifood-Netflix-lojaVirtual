package com.br.ecommerce.activity.loja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityLojaRecebimentosBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Loja;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LojaRecebimentosActivity extends AppCompatActivity {

    private ActivityLojaRecebimentosBinding binding;

    private Loja loja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaRecebimentosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
        recuperaLoja();
    }

    private void configCliques() {
        binding.include.textTitulo.setText("Recebimentos");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());

        binding.btnSalvar.setOnClickListener(v -> {
            ocultaTeclado();
            if (loja != null) {
                validaDados();
            } else {
                Toast.makeText(this, "Recuperando os dados da empresa, aguarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recuperaLoja() {
        DatabaseReference lojaRef = FirebaseHelper.getDatabaseReference()
                .child("loja");
        lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loja = snapshot.getValue(Loja.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        if (loja.getPublicKey() != null) {
            binding.edtPublicKey.setText(loja.getPublicKey());
        }

        if (loja.getAccessToken() != null) {
            binding.edtAcessToken.setText(loja.getAccessToken());
        }

        if (loja.getParcelas() != 0) {
            binding.edtParcelas.setText(String.valueOf(loja.getParcelas()));
        }
    }

    private void validaDados() {
        String publicKey = binding.edtPublicKey.getText().toString().trim();
        String accessToken = binding.edtAcessToken.getText().toString().trim();
        String parcelasStr = binding.edtParcelas.getText().toString().trim();
        int parcelas = 0;
        if (!parcelasStr.isEmpty()) {
            parcelas = Integer.parseInt(binding.edtParcelas.getText().toString().trim());
        }

        if (!publicKey.isEmpty()) {
            if (!accessToken.isEmpty()) {
                if (parcelas > 0 && parcelas <= 12) {

                    loja.setPublicKey(publicKey);
                    loja.setAccessToken(accessToken);
                    loja.setParcelas(parcelas);

                    loja.salvar();

                } else {
                    binding.edtParcelas.requestFocus();
                    binding.edtParcelas.setError("Mínimo 1 e máximo 12.");
                }
            } else {
                binding.edtPublicKey.requestFocus();
                binding.edtPublicKey.setError("Informe seu access token.");
            }
        } else {
            binding.edtPublicKey.requestFocus();
            binding.edtPublicKey.setError("Informe sua public key.");
        }
    }

    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtPublicKey.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}