package com.biamailov3.ifoodclone.activity.empresa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Categoria;
import com.biamailov3.ifoodclone.model.Produto;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmpresaFormProdutoActivity extends AppCompatActivity {

    private final int REQUEST_CATEGORIA = 100;
    private final int REQUEST_GALERIA = 200;

    private ImageView imgProduto;
    private EditText edtNome;
    private CurrencyEditText edtValor;
    private CurrencyEditText edtValorAntigo;
    private Button btnCategoria;
    private EditText edtDescricao;
    private LinearLayout lEdtDescricao;
    private TextView textToolbar;

    private Categoria categoriaSelecionada = null;

    private Produto produto;

    private String caminhoImagem;

    private Boolean novoProduto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_form_produto);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            produto = (Produto) bundle.getSerializable("produtoSelecionado");

            configDados();
        }

        configCliques();

    }

    private void configDados() {
        Picasso.get().load(produto.getUrlImagem()).into(imgProduto);
        edtNome.setText(produto.getNome());
        edtValor.setText(String.valueOf(produto.getValor()));
        edtValorAntigo.setText(String.valueOf(produto.getValorAntigo()));
        edtDescricao.setText(produto.getDescricao());

        recuperarCategoria();

        novoProduto = false;
        textToolbar.setText("Edição");
    }

    private void recuperarCategoria() {
        DatabaseReference categoriasRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(FirebaseHelper.getIdFirebase())
                .child(produto.getIdCategoria());
        categoriasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Categoria categoria = snapshot.getValue(Categoria.class);
                if (categoria != null) {
                    btnCategoria.setText(categoria.getNome());
                    categoriaSelecionada = categoria;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        btnCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmpresaCategoriasActivity.class);
            intent.putExtra("acesso", 1);
            startActivityForResult(intent, REQUEST_CATEGORIA);
        });

        lEdtDescricao.setOnClickListener(v -> {
            mostrarTelclado();
            edtDescricao.requestFocus();
        });

        imgProduto.setOnClickListener(v -> verificarPermissaoGaleria());
    }

    public void validarDados(View view) {
        String nome = edtNome.getText().toString().trim();
        double valor = (double) edtValor.getRawValue() / 100;
        double valorAntigo = (double) edtValorAntigo.getRawValue() / 100;
        String descricao = edtDescricao.getText().toString().trim();

        if (!nome.isEmpty()) {
            if (valor > 0) {
                if (categoriaSelecionada != null) {
                    if (!descricao.isEmpty()) {

                        ocultarTeclado();

                        if (produto == null) produto = new Produto();
                        produto.setIdEmpresa(FirebaseHelper.getIdFirebase());
                        produto.setNome(nome);
                        produto.setValor(valor);
                        produto.setValorAntigo(valorAntigo);
                        produto.setIdCategoria(categoriaSelecionada.getId());
                        produto.setDescricao(descricao);

                        if (novoProduto) {
                            if (caminhoImagem != null) {
                                salvarImagemFirebase();
                            } else {
                                ocultarTeclado();
                                Snackbar.make(
                                        imgProduto,
                                        "Selecione a imagem para o produto.",
                                        Snackbar.LENGTH_SHORT
                                ).show();
                            }
                        } else {
                            if (caminhoImagem != null) {
                                salvarImagemFirebase();
                            } else {
                                produto.salvar();
                            }
                        }

                    } else {
                        edtDescricao.requestFocus();
                        edtDescricao.setError("Informe uma descrição.");
                    }
                } else {
                    ocultarTeclado();
                    erroSalvarProduto("Selecione uma categoria para o produto.");
                }
            } else {
                edtValor.requestFocus();
                edtValor.setError("Informe um valor válido.");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Informe um nome.");
        }

    }

    private void verificarPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão negada.", Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Permissão negada.")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(produto.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            produto.setUrlImagem(task.getResult().toString());
            produto.salvar();

            if (novoProduto) {
                finish();
            }

        })).addOnFailureListener(e -> erroSalvarProduto(e.getMessage()));
    }

    private void erroSalvarProduto(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void iniciarComponentes() {
        textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Novo produto");

        imgProduto = findViewById(R.id.img_produto);
        edtNome = findViewById(R.id.edt_nome);

        edtValor = findViewById(R.id.edt_valor);
        edtValor.setLocale(new Locale("PT", "br"));

        edtValorAntigo = findViewById(R.id.edt_valor_antigo);
        edtValorAntigo.setLocale(new Locale("PT", "br"));

        btnCategoria = findViewById(R.id.btn_categoria);
        edtDescricao = findViewById(R.id.edt_descricao);
        lEdtDescricao = findViewById(R.id.l_edt_descricao);
    }

    private void mostrarTelclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                edtNome.getWindowToken(), 0
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CATEGORIA) {
                categoriaSelecionada = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                btnCategoria.setText(categoriaSelecionada.getNome());
            } else if (requestCode == REQUEST_GALERIA) {
                Bitmap bitmap;

                Uri imagemSelecionada = data.getData();
                caminhoImagem = data.getData().toString();

                if (Build.VERSION.SDK_INT < 28) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        imgProduto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        imgProduto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}