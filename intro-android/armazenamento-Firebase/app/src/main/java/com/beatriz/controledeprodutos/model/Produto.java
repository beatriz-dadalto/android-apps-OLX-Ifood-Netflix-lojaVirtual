package com.beatriz.controledeprodutos.model;

import com.beatriz.controledeprodutos.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Produto implements Serializable {

    private String id;
    private String nome;
    private int estoque;
    private double valor;
    private String urlImagem;

    public Produto() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        // pegue o id gerado pelo push e insira no setId. **push gera id**
        this.setId(reference.push().getKey()); // id produto
    }

    public void salvarProduto() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference() // nó principal
                .child("produtos") // criar um filho
                .child(FirebaseHelper.getIdFirebase()) // id user
                .child(this.id); // id produto
        reference.setValue(this);
    }

    public void deletarProduto() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference() // nó principal
                .child("produtos") // criar um filho
                .child(FirebaseHelper.getIdFirebase()) // id user
                .child(this.id); // id produto
        reference.removeValue();

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens") // cria uma pasta no firebase
                .child("produtos") // cria uma pasta no firebase
                .child(FirebaseHelper.getIdFirebase()) // cria uma pasta com o id
                .child(this.id + ".jpg"); // poe a imagem la
        storageReference.delete();
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

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
