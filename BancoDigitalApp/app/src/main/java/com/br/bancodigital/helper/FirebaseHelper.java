package com.br.bancodigital.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    public static StorageReference getStorageReference(){
        if(storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

    public static String getIdFirebase(){
        return getAuth().getUid();
    }

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseAuth getAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static boolean getAutenticado(){
        return getAuth().getCurrentUser() != null;
    }

    public static String validaErros(String erro){
        String mensagem = "";

        if(erro.contains("There is no user record corresponding to this identifier")){
            mensagem = "Nenhuma conta encontrada com este e-mail.";
        }else if(erro.contains("The email address is badly formatted")){
            mensagem = "Insira um e-mail válido.";
        }else if(erro.contains("The password is invalid or the user does not have a password")){
            mensagem = "Senha inválida, tente novamente.";
        }else if(erro.contains("The email address is already in use by another account")){
            mensagem = "Este e-mail já está em uso.";
        }else if(erro.contains("Password should be at least 6 characters")){
            mensagem = "Insira uma senha mais forte.";
        }

        return mensagem;
    }

}
