package com.br.ecommerce.activity.usuario;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.EnderecoSelecaoAdapter;
import com.br.ecommerce.databinding.ActivityUsuarioSelecionaEnderecoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioSelecionaEnderecoActivity extends AppCompatActivity implements EnderecoSelecaoAdapter.OnClickListener {

    private ActivityUsuarioSelecionaEnderecoBinding binding;
    private EnderecoSelecaoAdapter enderecoSelecaoAdapter;
    private List<Endereco> enderecoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioSelecionaEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarComponentes();
        configCliques();
        configRv();
        recuperaEndereco();
    }

    private void configRv() {
        binding.rvEnderecos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvEnderecos.setHasFixedSize(true);
        enderecoSelecaoAdapter = new EnderecoSelecaoAdapter(enderecoList, this);
        binding.rvEnderecos.setAdapter(enderecoSelecaoAdapter);
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
        binding.btnCadastrarEndereco.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, UsuarioFormEnderecoActivity.class));
        });
    }

    private void recuperaEndereco() {
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

                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhum endereço cadastrado");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(enderecoList);
                enderecoSelecaoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciarComponentes() {
        binding.include.textTitulo.setText("Endereço de entrega");
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Endereco endereco = (Endereco) result.getData().getSerializableExtra("enderecoCadastrado");
                    enderecoList.add(endereco);
                    binding.textInfo.setText("");
                    enderecoSelecaoAdapter.notifyItemInserted(enderecoList.size());
                }
            }
    );

    @Override
    public void onClick(Endereco endereco) {
        Intent intent = new Intent();
        intent.putExtra("enderecoSelecionado", endereco);
        setResult(RESULT_OK, intent);
        finish();
    }

}