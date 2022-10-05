package com.br.netflix.model;

import android.widget.Toast;

import com.br.netflix.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class MinhaLista implements Serializable {

    public static void salvar(List<String> minhaListaList) {
        DatabaseReference minhaListaRef = FirebaseHelper.getDatabaseReference()
                .child("minhaLista");
        minhaListaRef.setValue(minhaListaList);
    }
}
