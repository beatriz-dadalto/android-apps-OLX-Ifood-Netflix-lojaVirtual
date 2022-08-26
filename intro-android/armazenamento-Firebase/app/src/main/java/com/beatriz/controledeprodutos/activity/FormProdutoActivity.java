package com.beatriz.controledeprodutos.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beatriz.controledeprodutos.helper.FirebaseHelper;
import com.beatriz.controledeprodutos.model.Produto;
import com.beatriz.controledeprodutos.R;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FormProdutoActivity extends AppCompatActivity {

    private EditText editProduto;
    private EditText editQuantidade;
    private EditText editValor;
    private ImageView imagemProduto;
    private String caminhoImagem; // caminho da galeria do dispositivo
    private Bitmap imagem;

    private static final int REQUEST_GALERIA = 100;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);

        iniciarComponentes();

        // bundle para verificar se na intent tem dados recebido de outra activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // produto é a chave que coloquei na intent
            produto = (Produto) bundle.getSerializable("produto");

            editarProduto();
        }

    }

    public void verificarPermissaoGaleria(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormProdutoActivity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void showDialogPermissao(PermissionListener listener, String[] permissoes) {
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Você negou a permissão para acessar a galeria do dispositivo. Deseja Permitir?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // quem requisitou foi meu metodo de Galeria?
            if (requestCode == REQUEST_GALERIA) {
                Uri localImagemSelecionada = data.getData();
                caminhoImagem = localImagemSelecionada.toString();

                if (Build.VERSION.SDK_INT < 28) {
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImagemSelecionada);
                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imagemProduto.setImageBitmap(imagem);
            }

        }
    }

    private void iniciarComponentes() {
        editProduto = findViewById(R.id.edit_produto);
        editQuantidade = findViewById(R.id.edit_quantidade);
        editValor = findViewById(R.id.edit_valor);
        imagemProduto = findViewById(R.id.imagem_produto);
    }

    private void editarProduto() {
        editProduto.setText(produto.getNome());
        editQuantidade.setText(String.valueOf(produto.getEstoque()));
        editValor.setText(String.valueOf(produto.getValor()));
        Picasso.get().load(produto.getUrlImagem()).into(imagemProduto);
    }

    public void salvarProduto(View view) {
        // pegar os inputs do user
        String nome = editProduto.getText().toString();
        String quantidade = editQuantidade.getText().toString();
        String valor = editValor.getText().toString();

        // validar
        if (!nome.isEmpty()) {
            if (!quantidade.isEmpty()) {
                int qtd = Integer.parseInt(quantidade);
                if (qtd >= 1) {
                    if (!valor.isEmpty()) {
                        double valorProduto = Double.parseDouble(valor);
                        if (valorProduto > 0) {
                            Toast.makeText(this, "Atualizado", Toast.LENGTH_SHORT).show();

                            if (produto == null) {
                                produto = new Produto();
                            }
                            produto.setNome(nome);
                            produto.setEstoque(qtd);
                            produto.setValor(valorProduto);

                            if (caminhoImagem == null) {
                                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_SHORT).show();
                            } else {
                                salvarImagemProduto();
                            }

                            // termina e volta pra tela de listagem
                            finish();

                        } else {
                            editValor.requestFocus();
                            editValor.setError("Informe um valor maior que zero");
                        }
                    } else {
                        editValor.requestFocus();
                        editValor.setError("Informe o valor do produto");
                    }
                } else {
                    editQuantidade.requestFocus();
                    editQuantidade.setError("Informe um valor maior que zero");
                }
            } else {
                editQuantidade.requestFocus();
                editQuantidade.setError("Informe um valor válido");
            }
        } else {
            editProduto.requestFocus();
            editProduto.setError("informe o nome do produto");
        }
    }

    public void salvarImagemProduto() {
        StorageReference reference = FirebaseHelper.getStorageReference()
                .child("imagens") // cria uma pasta no firebase
                .child("produtos") // cria uma pasta no firebase
                .child(FirebaseHelper.getIdFirebase()) // cria uma pasta com o id
                .child(produto.getId() + ".jpg"); // poe a imagem la

        UploadTask uploadTask = reference.putFile(Uri.parse(caminhoImagem));

        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnCompleteListener(task -> {
            produto.setUrlImagem(task.getResult().toString());
            produto.salvarProduto();

            finish(); // fechar a tela
        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}