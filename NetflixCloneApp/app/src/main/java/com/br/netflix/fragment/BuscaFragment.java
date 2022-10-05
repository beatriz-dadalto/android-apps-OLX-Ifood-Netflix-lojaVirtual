package com.br.netflix.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.netflix.R;
import com.br.netflix.adapter.AdapterBusca;
import com.br.netflix.helper.FirebaseHelper;
import com.br.netflix.model.Categoria;
import com.br.netflix.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuscaFragment extends Fragment {

    private AdapterBusca adapterBusca;
    private List<Post> postList = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView rvPosts;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciaComponentes(view);
        configRv();
        recuperaPosts();
    }

    private void recuperaPosts() {
        DatabaseReference postsRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                    }
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum post cadastrado!");
                }

                progressBar.setVisibility(View.GONE);
                adapterBusca.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setHasFixedSize(true);
        adapterBusca = new AdapterBusca(postList, getContext());
        rvPosts.setAdapter(adapterBusca);
    }


    private void iniciaComponentes(View view) {
        searchView = view.findViewById(R.id.searchView);
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.textInfo);
    }
}