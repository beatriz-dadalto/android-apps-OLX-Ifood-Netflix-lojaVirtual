package com.biamailov3.ifoodclone.model;

import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class Categoria {

    private String id;
    private String nome;

    public Categoria() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference();
        setId(categoriaRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        categoriaRef.setValue(this);
    }

    public void remover() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        categoriaRef.removeValue();
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
}
