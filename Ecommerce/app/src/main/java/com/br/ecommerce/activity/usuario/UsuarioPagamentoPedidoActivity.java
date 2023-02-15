package com.br.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.br.ecommerce.DAO.ItemDAO;
import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityUsuarioPagamentoPedidoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Endereco;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Loja;
import com.br.ecommerce.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioPagamentoPedidoActivity extends AppCompatActivity {

    private ActivityUsuarioPagamentoPedidoBinding binding;
    private Endereco enderecoSelecionado;
    private Usuario usuario;
    private Loja loja;

    private ItemPedidoDAO itemPedidoDAO;
    private ItemDAO itemDAO;
    private List<ItemPedido> itemPedidoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioPagamentoPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperaDados();
    }

    private void recuperaDados() {
        itemPedidoDAO = new ItemPedidoDAO(this);
        itemDAO = new ItemDAO(this);
        itemPedidoList = itemPedidoDAO.getList();

        recuperaUsuario();
        recuperaLoja();
        getExtra();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            enderecoSelecionado = (Endereco) bundle.getSerializable("enderecoSelecionado");
        }
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}