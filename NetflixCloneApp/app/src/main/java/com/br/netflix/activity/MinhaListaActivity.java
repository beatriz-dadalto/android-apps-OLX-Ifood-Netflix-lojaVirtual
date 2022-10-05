package com.br.netflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.netflix.R;
import com.br.netflix.adapter.AdapterDownload;
import com.br.netflix.adapter.AdapterMinhaLista;
import com.br.netflix.helper.FirebaseHelper;
import com.br.netflix.model.Download;
import com.br.netflix.model.MinhaLista;
import com.br.netflix.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinhaListaActivity extends AppCompatActivity implements AdapterMinhaLista.OnClickListener {

    private List<Post> postList = new ArrayList<>();
    private List<String> minhaListaList = new ArrayList<>();
    private AdapterMinhaLista adapterMinhaLista;

    private RecyclerView rvPosts;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_lista);

        iniciaComponentes();
        configCliques();
        recuperaMinhaLista();
        configRv();
    }

    private void configCliques() {
        findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());
    }

    private void configRv() {
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setHasFixedSize(true);
        adapterMinhaLista = new AdapterMinhaLista(postList, this);
        rvPosts.setAdapter(adapterMinhaLista);
    }

    private void recuperaMinhaLista() {
        DatabaseReference minhaListaRef = FirebaseHelper.getDatabaseReference()
                .child("minhaLista");
        minhaListaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    minhaListaList.add(ds.getValue(String.class));
                }

                listIsEmpty();

                recuperaPosts(minhaListaList);
                Collections.reverse(postList);
                adapterMinhaLista.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaPosts(List<String> minhaListaList) {
        DatabaseReference postsRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    if (minhaListaList.contains(post.getId())) {
                        postList.add(post);
                    }
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(postList);
                adapterMinhaLista.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listIsEmpty() {
        if (!minhaListaList.isEmpty()) {
            textInfo.setText("");
        } else {
            textInfo.setText("Nenhum item em sua lista.");
        }
    }

    private void iniciaComponentes() {
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText("Minha Lista");

        rvPosts = findViewById(R.id.rvPosts);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
    }

    @Override
    public void onItemClickListener(Post post) {
        postList.remove(post);
        adapterMinhaLista.notifyDataSetChanged();

        minhaListaList.remove(post.getId());
        MinhaLista.salvar(minhaListaList);

        listIsEmpty();
    }
}