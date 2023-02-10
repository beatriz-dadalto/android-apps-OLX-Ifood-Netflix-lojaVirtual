package com.br.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.api.CEPService;
import com.br.ecommerce.databinding.ActivityUsuarioEnderecoBinding;
import com.br.ecommerce.databinding.ActivityUsuarioFormEnderecoBinding;
import com.br.ecommerce.model.Endereco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioFormEnderecoActivity extends AppCompatActivity {

    private ActivityUsuarioFormEnderecoBinding binding;

    private Endereco endereco;
    private boolean novoEndereco = true;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioFormEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();
        configCliques();
        getExtra();
        iniciaRetrofit();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            endereco = (Endereco) bundle.getSerializable("enderecoSelecionado");
            configDados();
            novoEndereco = false;
        }
    }

    private void iniciaRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void configDados() {
        binding.edtNomeEndereco.setText(endereco.getNomeEndereco());
        binding.edtCEP.setText(endereco.getCep());
        binding.edtUF.setText(endereco.getUf());
        binding.edtNumEndereco.setText(endereco.getNumero());
        binding.edtLogradouro.setText(endereco.getLogradouro());
        binding.edtBairro.setText(endereco.getBairro());
        binding.edtMunicipio.setText(endereco.getLocalidade());
    }

    private void configCliques() {
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());
        binding.include.btnSalvar.setOnClickListener(view -> validarDados());
        binding.btnBuscar.setOnClickListener(view -> buscarCEP());
    }

    private void buscarCEP() {

        String cep = binding.edtCEP.getMasked().replace("-", "").replaceAll("_", "");

        if (cep.length() == 8) {
            ocultaTeclado();
            binding.progressBar.setVisibility(View.VISIBLE);
            CEPService cepService = retrofit.create(CEPService.class);
            Call<Endereco> call = cepService.recuperarCEP(cep);

            call.enqueue(new Callback<Endereco>() {
                @Override
                public void onResponse(@NonNull Call<Endereco> call, @NonNull Response<Endereco> response) {
                    if (response.isSuccessful()) {

                        String nomeEndereco = "";
                        String numEndereco = "";
                        String idEndereco = "";
                        if (!novoEndereco) {
                            nomeEndereco = endereco.getNomeEndereco();
                            numEndereco = endereco.getNumero();
                            idEndereco = endereco.getId();
                        }

                        endereco = response.body();
                        
                        if (endereco != null) {
                            if (endereco.getLocalidade() != null) {

                                if (!novoEndereco) endereco.setId(idEndereco);
                                endereco.setNomeEndereco(nomeEndereco);
                                endereco.setNumero(numEndereco);

                                configDados();
                                
                            } else {
                                Toast.makeText(getBaseContext(), "Não foi possível recuperar o endereço", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Não foi possível recuperar o endereco", Toast.LENGTH_SHORT).show();
                        }
                    }

                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<Endereco> call, @NonNull Throwable t) {
                    Toast.makeText(getBaseContext(), "Não foi possível recuperar o endereco", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(this, "Formato do CEP inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void validarDados() {
        String nomeEndereco = binding.edtNomeEndereco.getText().toString().trim();
        String cep = binding.edtCEP.getMasked();
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
                                    binding.progressBar.setVisibility(View.GONE);
                                    if (novoEndereco) {
                                        Intent intent = new Intent();
                                        intent.putExtra("enderecoCadastrado", endereco);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }

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