package com.beatriz.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.adapter.AdapterAnuncios;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusAnunciosActivity extends AppCompatActivity implements AdapterAnuncios.OnClick {

    private List<Anuncio> anuncioList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView textInfo;
    private SwipeableRecyclerView rvAnuncios;
    private AdapterAnuncios adapterAnuncios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        iniciarComponentes();
        configRv();
        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarAnuncios();
    }

    private void configCliques() {
        findViewById(R.id.ib_add).setOnClickListener(view -> {
            startActivity(new Intent(this, FormAnuncioActivity.class));
        });
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    //se tiver mais de uma recyclerView criar outro metodo com o nome dela
    private void configRv() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(this));
        rvAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncioList, this);
        rvAnuncios.setAdapter(adapterAnuncios);

        rvAnuncios.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(position);
            }
        });
    }

    private void showDialogDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Anúncio?");
        builder.setMessage("Aperte 'sim' para deletar ou 'não' para cancelar");
        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.dismiss();
            adapterAnuncios.notifyDataSetChanged();
        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // position = sim ou nao que o user escolheu
            anuncioList.get(position).deletar();
            adapterAnuncios.notifyItemRemoved(position);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void recuperarAnuncios() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    anuncioList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        Anuncio anuncio = snap.getValue(Anuncio.class);
                        anuncioList.add(anuncio);
                    }
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum anúncio cadastrado");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciarComponentes() {
        TextView textTitulo = findViewById(R.id.text_titulo_anuncio);
        textTitulo.setText("Meus Anúncios");

        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.text_info);
        rvAnuncios = findViewById(R.id.rv_anuncios);
    }

    @Override
    public void onClickListener(Anuncio anuncio) {
        Intent intent = new Intent(this, FormAnuncioActivity.class);
        intent.putExtra("anuncio", anuncio);
        startActivity(intent);
    }
}