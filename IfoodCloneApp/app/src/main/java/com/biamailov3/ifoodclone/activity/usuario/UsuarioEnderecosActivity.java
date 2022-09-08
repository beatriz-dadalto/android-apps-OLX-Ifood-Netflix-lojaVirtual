package com.biamailov3.ifoodclone.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.EnderecoAdapter;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Categoria;
import com.biamailov3.ifoodclone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioEnderecosActivity extends AppCompatActivity implements EnderecoAdapter.OnClickListener {

    private EnderecoAdapter enderecoAdapter;
    private List<Endereco> enderecoList = new ArrayList<>();

    private SwipeableRecyclerView rvEnderecos;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_enderecos);

        inciarComponentes();
        configCliques();
        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarEnderecos();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.ib_add).setOnClickListener(view ->
                startActivity(new Intent(this, UsuarioFormEnderecoActivity.class)));
    }

    private void configRv() {
        rvEnderecos.setLayoutManager(new LinearLayoutManager(this));
        rvEnderecos.setHasFixedSize(true);
        enderecoAdapter = new EnderecoAdapter(enderecoList, this);
        rvEnderecos.setAdapter(enderecoAdapter);

        rvEnderecos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                dialogRemoverEndereco(enderecoList.get(position));
            }
        });
    }

    private void recuperarEnderecos() {
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
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum endereço cadastrado");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(enderecoList);
                enderecoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogRemoverEndereco(Endereco endereco) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover endereço");
        builder.setMessage("Deseja remover o endereço selecionado?");
        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.dismiss();
            enderecoAdapter.notifyDataSetChanged();
        });
        builder.setPositiveButton("Sim", ((dialog, which) -> {
            endereco.remover();
            enderecoList.remove(endereco);

            if (enderecoList.isEmpty()) {
                textInfo.setText("Nenhum endereço cadastrado");
            }

            enderecoAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }));


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void inciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Endereços");

        rvEnderecos = findViewById(R.id.rv_enderecos);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
    }

    @Override
    public void onClick(Endereco endereco) {
        Intent intent = new Intent(this, UsuarioFormEnderecoActivity.class);
        intent.putExtra("enderecoSelecionado", endereco);
        startActivity(intent);
    }
}