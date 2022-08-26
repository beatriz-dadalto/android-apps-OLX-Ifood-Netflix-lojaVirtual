package com.beatriz.olx_clone.model;

import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Favorito {

    private List<String> favoritos;

    public void salvar() {
        DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase());
        favoritosRef.setValue(getFavoritos());
    }

    public List<String> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<String> favoritos) {
        this.favoritos = favoritos;
    }
}
