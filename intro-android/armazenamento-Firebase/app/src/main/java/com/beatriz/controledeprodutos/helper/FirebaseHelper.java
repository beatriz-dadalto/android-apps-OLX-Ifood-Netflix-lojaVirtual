package com.beatriz.controledeprodutos.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    /*
     *  lib para gerenciar permissoes do app
     *  https://github.com/ParkSangGwon/TedPermission
     */

    public static StorageReference getStorageReference() {
        if (storageReference == null) {
            // obtendo a referencia do banco de dados
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

    public static String getIdFirebase() {
        return getAuth().getUid();
    }

    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(); // nó
        }
        return databaseReference;
    }


    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static boolean isAutenticado() {
        return getAuth().getCurrentUser() != null;
    }

}
