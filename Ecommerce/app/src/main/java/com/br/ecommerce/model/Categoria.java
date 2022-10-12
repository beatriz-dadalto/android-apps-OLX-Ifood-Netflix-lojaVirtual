package com.br.ecommerce.model;

import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Categoria implements Serializable {

    private String id;
    private String nome;
    private String urlImagem;
    private boolean todas = false;

    public Categoria() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference();
        setId(categoriaRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(getId());
        categoriaRef.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public boolean isTodas() {
        return todas;
    }

    public void setTodas(boolean todas) {
        this.todas = todas;
    }
}
