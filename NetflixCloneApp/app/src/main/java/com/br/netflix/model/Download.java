package com.br.netflix.model;

import com.br.netflix.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Download implements Serializable {

    public static void salvar(List<String> downloadList) {
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        downloadRef.setValue(downloadList);
    }
}
