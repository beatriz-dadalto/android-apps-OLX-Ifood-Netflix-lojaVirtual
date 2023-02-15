package com.br.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.br.ecommerce.DAO.ItemDAO;
import com.br.ecommerce.DAO.ItemPedidoDAO;
import com.br.ecommerce.R;
import com.br.ecommerce.api.MercadoPagoService;
import com.br.ecommerce.databinding.ActivityUsuarioPagamentoPedidoBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Endereco;
import com.br.ecommerce.model.ItemPedido;
import com.br.ecommerce.model.Loja;
import com.br.ecommerce.model.Produto;
import com.br.ecommerce.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioPagamentoPedidoActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 100;

    private ActivityUsuarioPagamentoPedidoBinding binding;
    private Endereco enderecoSelecionado;
    private Usuario usuario;
    private Loja loja;

    private ItemPedidoDAO itemPedidoDAO;
    private ItemDAO itemDAO;
    private List<ItemPedido> itemPedidoList = new ArrayList<>();

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioPagamentoPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperaDados();

        iniciaRetrofit();
    }

    private void configJSON() {
        JsonObject dados = new JsonObject();
        JsonArray itemList = new JsonArray();

        JsonObject payer = new JsonObject();
        JsonObject phone = new JsonObject();
        JsonObject address = new JsonObject();
        JsonObject payment_methods = new JsonObject();


        String telefone = usuario.getTelefone()
                .replace("(", "")
                .replace(")", "")
                .replace(" ", "");
        phone.addProperty("area_code", telefone.substring(0, 2));
        phone.addProperty("number", telefone.substring(2, 12));

        address.addProperty("street_name", enderecoSelecionado.getLogradouro());
        if (enderecoSelecionado.getNumero() != null) {
            address.addProperty("street_number", enderecoSelecionado.getNumero());
        }
        address.addProperty("zip_code", enderecoSelecionado.getCep());
        payment_methods.addProperty("installments", loja.getParcelas());

        JsonObject item;
        for (ItemPedido itemPedido : itemPedidoList) {
            Produto produto = itemPedidoDAO.getProduto(itemPedido.getId());

            item = new JsonObject();

            item.addProperty("title", produto.getTitulo());
            item.addProperty("currency_id", "BRL");
            item.addProperty("picture_url", produto.getUrlsImagens().get(0).getCaminhoImagem());
            item.addProperty("quantity", itemPedido.getQuantidade());
            item.addProperty("unit_price", produto.getValorAtual());

            itemList.add(item);
            itemPedido.setNomeProduto(produto.getTitulo());

            dados.add("items", itemList);

            payer.addProperty("name", usuario.getNome());
            payer.addProperty("email", usuario.getEmail());
            payer.add("phone", phone);
            payer.add("address", address);

            dados.add("payer", payer);
            dados.add("payment_methods", payment_methods);

            efetuarPagamento(dados);
        }
    }

    private void iniciaRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mercadopago.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void efetuarPagamento(JsonObject dados) {

        String url = "checkout/preferences?access_token=" + loja.getAccessToken();

        MercadoPagoService mercadoPagoService = retrofit.create(MercadoPagoService.class);
        Call<JsonObject> call = mercadoPagoService.efetuarPagamento(url, dados);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                String id = response.body().get("id").getAsString();
                continuaPagamento(id);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    private void continuaPagamento(String idPagamento) {
        // codigo do mercado pago
        final AdvancedConfiguration advancedConfiguration =
                new AdvancedConfiguration.Builder().setBankDealsEnabled(false).build();

        new MercadoPagoCheckout.
                Builder(loja.getPublicKey(), idPagamento)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, REQUEST_CODE);
    }

    private void recuperaDados() {
        itemPedidoDAO = new ItemPedidoDAO(this);
        itemDAO = new ItemDAO(this);
        itemPedidoList = itemPedidoDAO.getList();

        recuperaUsuario();

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

                if (usuario != null) {
                    recuperaLoja();
                }

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

                if (loja != null) {
                    configJSON();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}