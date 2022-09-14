package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.DAO.EmpresaDAO;
import com.biamailov3.ifoodclone.DAO.ItemPedidoDAO;
import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.CarrinhoAdapter;
import com.biamailov3.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.ItemPedido;
import com.biamailov3.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener, ProdutoCarrinhoAdapter.OnClickListener {

    private List<Produto> produtoList = new ArrayList<>();

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;

    private RecyclerView rvProdutos;
    private RecyclerView rvAddMais;
    private LinearLayout llAddMais;

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        empresaDAO = new EmpresaDAO(getBaseContext());

        iniciaComponentes();

        configCliques();

        configRv();

        recuperaIdsItensAddMais();

    }

    private void recuperaIdsItensAddMais(){
        DatabaseReference addMaisRef = FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(empresaDAO.getEmpresa().getId());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> idsItensList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String idProduto = ds.getValue(String.class);
                        idsItensList.add(idProduto);
                    }

                    recuperaProdutos(idsItensList);

                }else {
                    llAddMais.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaProdutos(List<String> idsItensList) {
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(empresaDAO.getEmpresa().getId());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Produto produto = ds.getValue(Produto.class);
                    if(idsItensList.contains(produto.getId())) produtoList.add(produto);
                }

                Collections.reverse(produtoList);
                produtoCarrinhoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv(){
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rvProdutos.setAdapter(carrinhoAdapter);

        rvAddMais.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAddMais.setHasFixedSize(true);
        produtoCarrinhoAdapter = new ProdutoCarrinhoAdapter(produtoList, getBaseContext(), this);
        rvAddMais.setAdapter(produtoCarrinhoAdapter);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Sacola");

        rvProdutos = findViewById(R.id.rv_produtos);
        rvAddMais = findViewById(R.id.rv_add_mais);
        llAddMais = findViewById(R.id.ll_add_mais);
    }

    @Override
    public void onClick(ItemPedido itemPedido) { // RV principal
        Toast.makeText(this, itemPedido.getItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Produto produto) { // Peça mais

    }
}