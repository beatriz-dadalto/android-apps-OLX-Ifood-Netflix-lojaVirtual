package com.br.ecommerce.model;

import com.br.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Favorito implements Serializable {

    private List<String> idsProdutos;

    public static void salvar(List<String> idsProdutos) {
        DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase());
        favoritoRef.setValue(idsProdutos);
    }

    public List<String> getIdsProdutos() {
        return idsProdutos;
    }

    public void setIdsProdutos(List<String> idsProdutos) {
        this.idsProdutos = idsProdutos;
    }
}
