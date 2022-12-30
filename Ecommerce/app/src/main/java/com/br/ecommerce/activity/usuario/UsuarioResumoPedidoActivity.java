package com.br.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.br.ecommerce.DAO.ItemDAO;
import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityUsuarioResumoPedidoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.helper.GetMask;
import com.br.ecommerce.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioResumoPedidoActivity extends AppCompatActivity {

    private ActivityUsuarioResumoPedidoBinding binding;

    private List<Endereco> enderecoList = new ArrayList<>();

    private ItemPedidoDAO itemPedidoDAO;
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioResumoPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperaEndereco();
    }

    private void configDados() {
        itemDAO = new ItemDAO(this);
        itemPedidoDAO = new ItemPedidoDAO(this);

        binding.include.textTitulo.setText("Resumo Pedido");
        binding.include.include.ibVoltar.setOnClickListener(view -> finish());

        if (!enderecoList.isEmpty()) {
            Endereco endereco = enderecoList.get(0);

            StringBuilder enderecoCompleto = new StringBuilder();
            enderecoCompleto.append(endereco.getLogradouro())
                    .append(", ")
                    .append(endereco.getNumero())
                    .append("\n")
                    .append(endereco.getBairro())
                    .append(", ")
                    .append(endereco.getLocalidade())
                    .append("/")
                    .append(endereco.getUf())
                    .append("\n")
                    .append("CEP: ")
                            .append(endereco.getCep());

            binding.textEnderecoEntrega.setText(enderecoCompleto);
            binding.btnAlterarEndereco.setText("Alterar endereço de entrega");
        } else {
            binding.textEnderecoEntrega.setText("Nenhum endereço cadastrado");
            binding.btnAlterarEndereco.setText("Cadastrar endereço");
        }

        binding.textValorTotal.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalPedido())));
        binding.textValor.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalPedido())));
    }

    private void recuperaEndereco() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Endereco endereco = ds.getValue(Endereco.class);
                    enderecoList.add(endereco);
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(enderecoList);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}