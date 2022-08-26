package com.beatriz.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.activity.autenticacao.LoginActivity;
import com.beatriz.casaportemporada.adapter.AdapterAnuncios;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Anuncio;
import com.beatriz.casaportemporada.model.Filtro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterAnuncios.OnClick {

    private final int REQUEST_FILTRO = 100;

    private RecyclerView rvAnuncios;
    private TextView textInfo;
    private ProgressBar progressBar;

    private List<Anuncio> anuncioList = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private ImageButton ibMenu;

    private Filtro filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        configRv();
        recuperarAnuncios();
        configCliques();
    }

    //se tiver mais de uma recyclerView criar outro metodo com o nome dela
    private void configRv() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(this));
        rvAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncioList, this);
        rvAnuncios.setAdapter(adapterAnuncios);
    }

    private void recuperarAnuncios(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void recuperarAnunciosFiltro(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    anuncioList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        Anuncio anuncio = snap.getValue(Anuncio.class);

                        int quarto = Integer.parseInt(anuncio.getQuarto());
                        int banheiro = Integer.parseInt(anuncio.getBanheiro());
                        int garagem = Integer.parseInt(anuncio.getGaragem());

                        if(quarto >= filtro.getQuantidadeQuarto() &&
                                banheiro >= filtro.getQuantidadeBanheiro() &&
                                garagem >= filtro.getQuantidadeGaragem()) {
                            anuncioList.add(anuncio);
                        }

                    }
                }

                if(anuncioList.size() == 0) {
                    textInfo.setText("Nenhum anúncio encontrado.");
                } else {
                    textInfo.setText("");
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

    private void configCliques() {
        ibMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, ibMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_filtrar) {
                    Intent intent = new Intent(this, FiltrarAnunciosActivity.class);
                    intent.putExtra("filtro", filtro);
                    startActivityForResult(intent, REQUEST_FILTRO);
                } else if (menuItem.getItemId() == R.id.menu_meus_anuncios) {
                    if(FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MeusAnunciosActivity.class));
                    } else {
                        showDialogLogin();
                    }
                } else {
                    if(FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MinhaContaActivity.class));
                    } else {
                        showDialogLogin();
                    }
                }
                return true;
            });

            popupMenu.show();
        });
    }

    private void showDialogLogin()      {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Autenticação");
        builder.setMessage("Você não está logado no app. Deseja logar agora?");
        builder.setCancelable(false);
        builder.setNegativeButton("Não", ((dialog, which) -> dialog.dismiss()));
        builder.setPositiveButton("Sim", (dialog, which) -> {
           startActivity(new Intent(this, LoginActivity.class));
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciarComponentes() {
        ibMenu = findViewById(R.id.ib_menu);
        rvAnuncios = findViewById(R.id.rv_anuncios);
        textInfo = findViewById(R.id.text_info);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // deu certo pegar as info da tela Filtrar?
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_FILTRO) {
                filtro = (Filtro) data.getSerializableExtra("filtro");
                if(filtro.getQuantidadeQuarto() > 0 || filtro.getQuantidadeBanheiro() > 0 || filtro.getQuantidadeGaragem() > 0) {
                    // Recuperar anuncios com base nos filtros
                    recuperarAnunciosFiltro();
                }
            }
        } else {
            recuperarAnuncios();
        }
    }

    @Override
    public void onClickListener(Anuncio anuncio) {
        Intent intent = new Intent(this, DetalheAnuncioActivity.class);
        intent.putExtra("anuncio", anuncio);
        startActivity(intent);
    }

}