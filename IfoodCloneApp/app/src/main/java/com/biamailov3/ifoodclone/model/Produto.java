package com.biamailov3.ifoodclone.model;

import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Produto implements Serializable {

    private String id;
    private String nome;
    private int idLocal;
    private String idEmpresa;
    private String idCategoria;
    private Double valor;
    private Double valorAntigo;
    private String descricao;
    private String urlImagem;

    public Produto() {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference();
        setId(produtoRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        produtoRef.setValue(this);
    }

    public void remover() {
        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId());
        produtoRef.removeValue();

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(getId() + ".jpeg");
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

    // para nao salvar no firebase
    @Exclude
    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorAntigo() {
        return valorAntigo;
    }

    public void setValorAntigo(Double valorAntigo) {
        this.valorAntigo = valorAntigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
