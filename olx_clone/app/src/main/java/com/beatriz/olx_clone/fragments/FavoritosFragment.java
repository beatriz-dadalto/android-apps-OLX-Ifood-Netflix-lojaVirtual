package com.beatriz.olx_clone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.activity.DetalhesAnunciosActivity;
import com.beatriz.olx_clone.adapter.AnuncioAdapter;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritosFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private AnuncioAdapter anuncioAdapter;
    private List<Anuncio> anuncioList = new ArrayList<>();
    private RecyclerView rvAnuncios;
    private ProgressBar progressBar;
    private TextView textInfo;
    private Button btnLogar;
    private List<String> favoritosList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        iniciarComponentes(view);
        configRv();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarFavoritos();
    }

    private void iniciarComponentes(View view) {
        rvAnuncios = view.findViewById(R.id.rv_anuncios);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
        btnLogar = view.findViewById(R.id.btn_logar);
    }

    private void recuperarFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favoritosList.clear();
                        anuncioList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            favoritosList.add(ds.getValue(String.class));
                        }

                        if (favoritosList.size() > 0) {
                            recuperarAnuncios();
                        } else {
                            textInfo.setText("Nenhum anúncio favoritado");
                            progressBar.setVisibility(View.GONE);
                            anuncioAdapter.notifyDataSetChanged();
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            textInfo.setText("");
            progressBar.setVisibility(View.GONE);
            btnLogar.setVisibility(View.VISIBLE);
        }
    }

    private void recuperarAnuncios() {
        for (int i = 0; i < favoritosList.size(); i++) {
            DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                    .child("anuncios_publicos")
                    .child(favoritosList.get(i));
            anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Anuncio anuncio = snapshot.getValue(Anuncio.class);
                    anuncioList.add(anuncio);

                    if (anuncioList.size() == favoritosList.size()) {
                        textInfo.setText("");
                        Collections.reverse(anuncioList);
                        progressBar.setVisibility(View.GONE);
                        anuncioAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configRv() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAnuncios.setHasFixedSize(true);
        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rvAnuncios.setAdapter(anuncioAdapter);
    }

    @Override
    public void onClick(Anuncio anuncio) {
        Intent intent = new Intent(requireContext(), DetalhesAnunciosActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);
        startActivity(intent);
    }
}