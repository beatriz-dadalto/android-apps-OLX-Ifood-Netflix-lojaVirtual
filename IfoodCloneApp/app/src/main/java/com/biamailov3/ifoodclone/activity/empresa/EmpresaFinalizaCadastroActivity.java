package com.biamailov3.ifoodclone.activity.empresa;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Empresa;
import com.biamailov3.ifoodclone.model.Login;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmpresaFinalizaCadastroActivity extends AppCompatActivity {

    private final int REQUEST_GALERIA = 100;

    private ImageView imgLogo;
    private EditText edtNome;
    private MaskEditText edtTelefone;
    private CurrencyEditText edtTaxaEntrega;
    private CurrencyEditText edtPedidoMinimo;
    private EditText edtTempoMinimo;
    private EditText edtTempoMaximo;
    private EditText edtCategoria;
    private ProgressBar progressBar;

    private String caminhoLogo = "";

    private Empresa empresa;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_finaliza_cadastro);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresa = (Empresa) bundle.getSerializable("empresa");
            login = (Login) bundle.getSerializable("login");
        }

        iniciarComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    public void validarDados(View view) {
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getUnMasked();
        double taxaEntrega = (double) edtTaxaEntrega.getRawValue() / 100;
        double pedidoMinimo = (double) edtPedidoMinimo.getRawValue() / 100;

        int tempoMinimo = 0;
        if (!edtTempoMinimo.getText().toString().isEmpty()) {
            tempoMinimo = Integer.parseInt(edtTempoMinimo.getText().toString());
        }
        int tempoMaximo = 0;
        if (!edtTempoMaximo.getText().toString().isEmpty()) {
            tempoMaximo = Integer.parseInt(edtTempoMaximo.getText().toString());
        }

        String categoria = edtCategoria.getText().toString().trim();

        if (!caminhoLogo.isEmpty()) {
            if (!nome.isEmpty()) {
                if (edtTelefone.isDone()) {
                    if (tempoMinimo > 0) {
                        if (tempoMaximo > 0) {
                            if (!categoria.isEmpty()) {

                                ocultarTeclado();
                                progressBar.setVisibility(View.VISIBLE);
                                empresa.setNome(nome);
                                empresa.setTelefone(telefone);
                                empresa.setTaxaEntrega(taxaEntrega);
                                empresa.setPedidoMinimo(pedidoMinimo);
                                empresa.setTempoMinEntrega(tempoMinimo);
                                empresa.setTempoMaxEntrega(tempoMaximo);
                                empresa.setCategoria(categoria);

                                salvarImagemFirebase();

                            } else {
                                edtCategoria.requestFocus();
                                edtCategoria.setError("Informe uma categoria");
                            }
                        } else {
                            edtTempoMaximo.requestFocus();
                            edtTempoMaximo.setError("Informe um tempo máximo de entrega");
                        }
                    } else {
                        edtTempoMinimo.requestFocus();
                        edtTempoMinimo.setError("Informe um tempo minímo de entrega");
                    }
                } else {
                    edtTelefone.requestFocus();
                    edtTelefone.setError("Informe um telefone");
                }
            } else {
                edtNome.requestFocus();
                edtNome.setError("Informe o nome da empresa");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            ocultarTeclado();
            erroAutenticacao("Selecione uma imagem para a sua empresa");
        }
    }

    public void selecionarLogo(View view) {
        verificarPermissaoGaleria();
    }

    private void verificarPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(EmpresaFinalizaCadastroActivity.this, "Permissão negada!", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Permissão Negada")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a galeria de fotos. Deseja ativar a permissão agora?")
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
                .child("perfil")
                .child(empresa.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoLogo));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {
            login.setAcesso(true);
            login.salvar();
            empresa.setUrlLogo(task.getResult().toString());
            empresa.salvar();
            finish();
            startActivity(new Intent(getBaseContext(), EmpresaHomeActivity.class));
        })).addOnFailureListener(e -> erroAutenticacao(e.getMessage()));
    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciarComponentes() {
        TextView textToolbar = findViewById(R.id.text_toolbar);
        textToolbar.setText("Finalizar Cadastro");

        imgLogo = findViewById(R.id.img_logo);
        edtNome = findViewById(R.id.edt_nome);
        edtTelefone = findViewById(R.id.edt_telefone);
        edtTaxaEntrega = findViewById(R.id.edt_taxa_entrega);
        edtTaxaEntrega.setLocale(new Locale("PT", "br"));
        edtPedidoMinimo = findViewById(R.id.edt_pedido_minimo);
        edtPedidoMinimo.setLocale(new Locale("PT", "br"));
        edtTempoMinimo = findViewById(R.id.edt_tempo_minimo);
        edtTempoMaximo = findViewById(R.id.edt_tempo_maximo);
        edtCategoria = findViewById(R.id.edt_categoria);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {
                // recuperar imagem da galeria que o user selecionou
                Bitmap bitmap;
                Uri imagemSelecionada = data.getData();
                caminhoLogo = data.getData().toString();

                if (Build.VERSION.SDK_INT < 28) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        imgLogo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        imgLogo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtNome.getWindowToken(), 0);
    }

}