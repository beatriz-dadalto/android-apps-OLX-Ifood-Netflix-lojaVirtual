package com.beatriz.olx_clone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.activity.DetalhesAnunciosActivity;
import com.beatriz.olx_clone.activity.FormAnuncioActivity;
import com.beatriz.olx_clone.adapter.AnuncioAdapter;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusAnunciosFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private AnuncioAdapter anuncioAdapter;
    private List<Anuncio> anuncioList = new ArrayList<>();
    private SwipeableRecyclerView rvAnuncios;
    private ProgressBar progressBar;
    private TextView textInfo;
    private Button btnLogar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        iniciarComponentes(view);
        configRv();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarAnuncios();
    }

    private void recuperarAnuncios() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                    .child("meus_anuncios")
                    .child(FirebaseHelper.getIdFirebase());
            anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        anuncioList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Anuncio anuncio = ds.getValue(Anuncio.class);
                            anuncioList.add(anuncio);
                        }
                        Collections.reverse(anuncioList);
                        anuncioAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        textInfo.setText("");
                    } else {
                        textInfo.setText("Nenhum anúncio cadastrado.");
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            btnLogar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            textInfo.setText("");
        }
    }

    private void configRv() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAnuncios.setHasFixedSize(true);
        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rvAnuncios.setAdapter(anuncioAdapter);

        rvAnuncios.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                showDialogDelete(anuncioList.get(position));
            }

            @Override
            public void onSwipedRight(int position) {
                showDialogEdit(anuncioList.get(position));
            }
        });
    }

    private void showDialogDelete(Anuncio anuncio) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja remover o anúncio?");
        alertDialog.setMessage("Clique em sim para remover o anúncio ou clique em fechar");
        alertDialog.setNegativeButton("Fechar", ((dialog, which) -> {
            dialog.dismiss();
            anuncioAdapter.notifyDataSetChanged();
        })).setPositiveButton("Sim", ((dialog, which) -> {
            anuncioList.remove(anuncio);
            anuncio.remover();
            anuncioAdapter.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void showDialogEdit(Anuncio anuncio) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja editar o anúncio?");
        alertDialog.setMessage("Clique em sim para editar o anúncio ou clique em fechar");
        alertDialog.setNegativeButton("Fechar", ((dialog, which) -> {
            dialog.dismiss();
            anuncioAdapter.notifyDataSetChanged();
        })).setPositiveButton("Sim", ((dialog, which) -> {
            Intent intent = new Intent(requireActivity(), FormAnuncioActivity.class);
            intent.putExtra("anuncioSelecionado", anuncio);
            startActivity(intent);

            anuncioAdapter.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void iniciarComponentes(View view) {
        rvAnuncios = view.findViewById(R.id.rv_anuncios);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
        btnLogar = view.findViewById(R.id.btn_logar);
    }

    @Override
    public void onClick(Anuncio anuncio) {
        Intent intent = new Intent(requireContext(), DetalhesAnunciosActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);
        startActivity(intent);
    }
}