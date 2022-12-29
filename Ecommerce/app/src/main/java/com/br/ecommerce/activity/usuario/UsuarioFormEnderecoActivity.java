package com.br.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.br.ecommerce.databinding.ActivityUsuarioEnderecoBinding;
import com.br.ecommerce.databinding.ActivityUsuarioFormEnderecoBinding;
import com.br.ecommerce.model.Endereco;

public class UsuarioFormEnderecoActivity extends AppCompatActivity {

    private ActivityUsuarioFormEnderecoBinding binding;

    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioFormEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
        binding.include.btnSalvar.setOnClickListener(view -> validarDados());
    }

    private void validarDados() {
        String nomeEndereco = binding.edtNomeEndereco.getText().toString().trim();
        String cep = binding.edtCEP.getText().toString().trim();
        String uf = binding.edtUF.getText().toString().trim();
        String numero = binding.edtNumEndereco.getText().toString().trim();
        String logradouro = binding.edtLogradouro.getText().toString().trim();
        String bairro = binding.edtBairro.getText().toString().trim();
        String municipio = binding.edtMunicipio.getText().toString().trim();

        if (!nomeEndereco.isEmpty()) {
            if (!cep.isEmpty()) {
                if (!uf.isEmpty()) {
                    if (!numero.isEmpty()) {
                        if (!logradouro.isEmpty()) {
                            if (!bairro.isEmpty()) {
                                if (!municipio.isEmpty()) {

                                    ocultaTeclado();

                                    binding.progressBar.setVisibility(View.VISIBLE);

                                    if (endereco == null) endereco = new Endereco();
                                    endereco.setNomeEndereco(nomeEndereco);
                                    endereco.setCep(cep);
                                    endereco.setUf(uf);
                                    endereco.setNumero(numero);
                                    endereco.setLogradouro(logradouro);
                                    endereco.setBairro(bairro);
                                    endereco.setLocalidade(municipio);

                                    endereco.salvar();
                                    finish();

                                } else {
                                    binding.edtMunicipio.requestFocus();
                                    binding.edtMunicipio.setError("Informação obrigatória");
                                }
                            } else {
                                binding.edtBairro.requestFocus();
                                binding.edtBairro.setError("Informação obrigatória");
                            }
                        } else {
                            binding.edtLogradouro.requestFocus();
                            binding.edtLogradouro.setError("Informação obrigatória");
                        }

                    } else {
                        binding.edtNumEndereco.requestFocus();
                        binding.edtNumEndereco.setError("Informação obrigatória");
                    }

                } else {
                    binding.edtUF.requestFocus();
                    binding.edtUF.setError("Informação obrigatória");
                }
            } else {
                binding.edtCEP.requestFocus();
                binding.edtCEP.setError("Informação obrigatória");
            }
        } else {
            binding.edtNomeEndereco.requestFocus();
            binding.edtNomeEndereco.setError("Informação obrigatória");
        }

    }

    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtNomeEndereco.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Novo endereço");
    }
}