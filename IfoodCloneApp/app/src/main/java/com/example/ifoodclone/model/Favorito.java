package com.example.ifoodclone.model;

import com.example.ifoodclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Favorito {

    private List<String> favoritoList = new ArrayList<>();

    public Favorito() {
    }

    public void salvar(){
        DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase());
        favoritosRef.setValue(getFavoritoList());
    }

    public List<String> getFavoritoList() {
        return favoritoList;
    }

    public void setFavoritoList(List<String> favoritoList) {
        this.favoritoList = favoritoList;
    }
}
