package com.br.netflix.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

public class BuscaFragment extends Fragment {

    private List<Post> postList = new ArrayList<>();
    private AdapterBusca adapterBusca;

    private SearchView searchView;
    private RecyclerView rvPosts;
    private ProgressBar progressBar;
    private TextView textInfo;
    private AlertDialog dialog;

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
        configRv(postList);
        recuperaPosts();
        configSearchView();
    }

    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisa) {
                if (pesquisa.length() >= 3) {
                    buscarPosts(pesquisa);
                } else {
                    ocultarTeclado();
                    showDialog("Digite no mínimo 3 letras.");
                }
                
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(view -> {
            ocultarTeclado();
            recuperaPosts();

            EditText edt = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            edt.getText().clear();
            searchView.clearFocus();
        });
    }

    private void buscarPosts(String pesquisa) {
        List<Post> postListBusca = new ArrayList<>();

        ocultarTeclado();

        for (Post post : postList) {
            if (post.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))) {
                postListBusca.add(post);
            }
        }

        if (!postListBusca.isEmpty()) {
            configRv(postListBusca);
        } else {
            configRv(new ArrayList<>());
            textInfo.setText("Nada foi encontrado com o nome pesquisado.");
        }
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
                configRv(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv(List<Post> postList) {
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setHasFixedSize(true);
        adapterBusca = new AdapterBusca(postList, getContext());
        rvPosts.setAdapter(adapterBusca);
        adapterBusca.notifyDataSetChanged();
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext(), R.style.CustomAlertDialog
        );

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void ocultarTeclado() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void iniciaComponentes(View view) {
        searchView = view.findViewById(R.id.searchView);
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.textInfo);
    }
}