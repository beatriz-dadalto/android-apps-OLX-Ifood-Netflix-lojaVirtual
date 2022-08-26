package com.beatriz.olx_clone.fragments;

import android.app.Activity;
import android.content.Intent;
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

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.activity.CategoriasActivity;
import com.beatriz.olx_clone.activity.DetalhesAnunciosActivity;
import com.beatriz.olx_clone.activity.EstadosActivity;
import com.beatriz.olx_clone.activity.FiltrosActivity;
import com.beatriz.olx_clone.activity.FormAnuncioActivity;
import com.beatriz.olx_clone.adapter.AnuncioAdapter;
import com.beatriz.olx_clone.autenticacao.LoginActivity;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.helper.SPFiltro;
import com.beatriz.olx_clone.model.Anuncio;
import com.beatriz.olx_clone.model.Categoria;
import com.beatriz.olx_clone.model.Filtro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private final int REQUEST_CATEGORIA = 100;

    private AnuncioAdapter anuncioAdapter;
    private List<Anuncio> anuncioList = new ArrayList<>();

    private RecyclerView rvAnuncios;
    private ProgressBar progressBar;
    private TextView textInfo;

    private Filtro filtro = new Filtro();

    private SearchView searchView;
    private EditText editSearchView;

    private Button btnRegioes;
    private Button btnCategorias;
    private Button btnFiltros;
    private Button btnNovoAnuncio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        iniciarComponentes(view);
        configRv();
        configCliques();
        configSearchView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        configFiltros();
    }

    private void configSearchView(){
        editSearchView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(v -> {
            limparPesquisa();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SPFiltro.setFiltro(requireActivity(), "pesquisa", query);

                configFiltros();

                ocultarTeclado();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void configFiltros(){
        filtro = SPFiltro.getFiltro(requireActivity());

        if(!filtro.getEstado().getRegiao().isEmpty()){
            btnRegioes.setText(filtro.getEstado().getRegiao());
        } else {
            btnRegioes.setText("Regiões");
        }

        if(!filtro.getCategoria().isEmpty()){
            btnCategorias.setText(filtro.getCategoria());
        } else {
            btnCategorias.setText("Categorias");
        }

        recuperarAnuncios();
    }

    private void limparPesquisa(){
        searchView.clearFocus();

        editSearchView.setText("");

        SPFiltro.setFiltro(requireActivity(), "pesquisa", "");

        configFiltros();

        ocultarTeclado();
    }

    private void recuperarAnuncios() {
        DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    anuncioList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Anuncio anuncio = ds.getValue(Anuncio.class);
                        anuncioList.add(anuncio);
                    }
                    // Filtro por categoria
                    if(!filtro.getCategoria().isEmpty()){
                        if(!filtro.getCategoria().equals("Todas as categorias")){
                            for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                                if(!anuncio.getCategoria().equals(filtro.getCategoria())){
                                    anuncioList.remove(anuncio);
                                }
                            }
                        }
                    }
                    // Filtro por Estado
                    if(!filtro.getEstado().getUf().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getLocal().getUf().contains(filtro.getEstado().getUf())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }
                    // Filtro por DDD
                    if(!filtro.getEstado().getDdd().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getLocal().getUf().equals(filtro.getEstado().getUf())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }
                    // Filtro por nome pesquisado
                    if(!filtro.getPesquisa().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getTitulo().toLowerCase().contains(filtro.getPesquisa().toLowerCase())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }
                    // Filtro por valor minimo
                    if(filtro.getValorMin() > 0){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(anuncio.getValor() < filtro.getValorMin()){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }
                    // Filtro por valor maximo
                    if(filtro.getValorMax() > 0){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(anuncio.getValor() > filtro.getValorMax()){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    if(anuncioList.isEmpty()){
                        textInfo.setText("Nenhum anúncio encontrado.");
                    }else {
                        textInfo.setText("");
                    }

                } else {
                    textInfo.setText("Nenhum anúncio cadastrado.");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                anuncioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAnuncios.setHasFixedSize(true);
        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rvAnuncios.setAdapter(anuncioAdapter);
    }

    private void iniciarComponentes(View view) {
        btnNovoAnuncio = view.findViewById(R.id.btn_novo_anuncio);
        rvAnuncios = view.findViewById(R.id.rv_anuncios);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.text_info);
        btnRegioes = view.findViewById(R.id.btn_regioes);
        btnCategorias = view.findViewById(R.id.btn_categorias);
        btnFiltros = view.findViewById(R.id.btn_filtros);
        searchView = view.findViewById(R.id.search_view);
    }

    private void configCliques() {
        btnNovoAnuncio.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {
                startActivity(new Intent(getActivity(), FormAnuncioActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        btnCategorias.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), CategoriasActivity.class);
            intent.putExtra("todas", true);
            startActivityForResult(intent, REQUEST_CATEGORIA);
        });

        btnFiltros.setOnClickListener(view -> startActivity(new Intent(requireActivity(), FiltrosActivity.class)));
        btnRegioes.setOnClickListener(view -> startActivity(new Intent(requireActivity(), EstadosActivity.class)));
    }

    private void ocultarTeclado(){
        InputMethodManager inputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(Anuncio anuncio) {
        Intent intent = new Intent(requireContext(), DetalhesAnunciosActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CATEGORIA){
                Categoria categoriaSelecionada = (Categoria) data.getExtras().getSerializable("categoriaSelecionada");
                SPFiltro.setFiltro(requireActivity(), "categoria", categoriaSelecionada.getNome());

                configFiltros();
            }
        }
    }
}