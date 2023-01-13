package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.EnderecoSelecaoAdapter;
import com.br.ecommerce.databinding.ActivityUsuarioSelecionaEnderecoBinding;
import com.br.ecommerce.model.Endereco;

import java.util.ArrayList;
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
    }

    private void configRv() {
        binding.rvEnderecos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvEnderecos.setHasFixedSize(true);
        enderecoSelecaoAdapter = new EnderecoSelecaoAdapter(enderecoList, this);
        binding.rvEnderecos.setAdapter(enderecoSelecaoAdapter);
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
    }

    private void iniciarComponentes() {
        binding.include.textTitulo.setText("Endereço de entrega");
    }

    @Override
    public void onClick(Endereco endereco) {
        Toast.makeText(this, endereco.getNomeEndereco(), Toast.LENGTH_SHORT).show();
    }
}