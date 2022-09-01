package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.CategoriaAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Categoria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaCategoriasActivity extends AppCompatActivity implements CategoriaAdapter.OnClickListener {

    private SwipeableRecyclerView rvCategorias;
    private ProgressBar progressBar;
    private TextView textInfo;

    private AlertDialog dialog;

    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();

    private Categoria categoriaSelecionada;
    private int categoriaIndex = 0;

    private Boolean novaCategoria = true;

    private int acesso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_categorias);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            acesso = bundle.getInt("acesso");
        }

        iniciarComponentes();
        configCliques();
        recuperarCategorias();
        configRv();
    }

    private void configRv() {
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(categoriaList, this);
        rvCategorias.setAdapter(categoriaAdapter);

        rvCategorias.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                dialogRemoverCategoria(categoriaList.get(position));
            }
        });
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_add).setOnClickListener(view -> {
            novaCategoria = true;
            showDialog();
        });
    }

    private void recuperarCategorias() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(FirebaseHelper.getIdFirebase());
        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Categoria categoria = ds.getValue(Categoria.class);
                        categoriaList.add(categoria);
                    }
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhuma categoria cadastrada");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(categoriaList);
                categoriaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_categoria, null);
        builder.setView(view);

        EditText edtCategoria = view.findViewById(R.id.edt_categoria);
        Button btnFechar = view.findViewById(R.id.btn_fechar);
        Button btnSalvar = view.findViewById(R.id.btn_salvar);

        if (!novaCategoria) {
            edtCategoria.setText(categoriaSelecionada.getNome());
        }

        btnSalvar.setOnClickListener(v -> {
            String nomeCategoria = edtCategoria.getText().toString().trim();

            if (!nomeCategoria.isEmpty()) {

                if (novaCategoria) {

                    Categoria categoria = new Categoria();
                    categoria.setNome(nomeCategoria);
                    categoria.salvar();

                    categoriaList.add(categoria);

                } else {
                    categoriaSelecionada.setNome(nomeCategoria);
                    categoriaList.set(categoriaIndex, categoriaSelecionada);
                    categoriaSelecionada.salvar();
                }

                if (!categoriaList.isEmpty()) {
                    textInfo.setText("");
                }

                dialog.dismiss();
                categoriaAdapter.notifyDataSetChanged();

            } else {
                edtCategoria.requestFocus();
                edtCategoria.setError("Informe uma categoria");
            }
        });

        btnFechar.setOnClickListener(v -> dialog.dismiss());

        dialog = builder.create();
        dialog.show();
    }

    private void dialogRemoverCategoria(Categoria categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover categoria");
        builder.setMessage("Deseja remover a categoria selecionada?");
        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.dismiss();
            categoriaAdapter.notifyDataSetChanged();
        });
        builder.setPositiveButton("Sim", ((dialog, which) -> {
            categoria.remover();
            categoriaList.remove(categoria);

            if (categoriaList.isEmpty()) {
                textInfo.setText("Nenhuma categoria cadastrada");
            }

            categoriaAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }));


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Categorias");

        rvCategorias = findViewById(R.id.rv_categorias);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
    }

    @Override
    public void onClick(Categoria categoria, int position) {
        if (acesso == 0) {
            categoriaSelecionada = categoria;
            categoriaIndex = position;
            novaCategoria = false;

            showDialog();
        } else if (acesso == 1) {
            Intent intent = new Intent();
            intent.putExtra("categoriaSelecionada", categoria);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}