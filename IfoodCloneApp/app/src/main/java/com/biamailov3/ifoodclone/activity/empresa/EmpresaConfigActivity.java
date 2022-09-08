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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.helper.FirebaseHelper;
import com.biamailov3.ifoodclone.model.Empresa;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmpresaConfigActivity extends AppCompatActivity {

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
    private ImageButton ibSalvar;

    private String caminhoLogo;

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_config);

        iniciarComponentes();
        recuperarEmpresa();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        ibSalvar.setOnClickListener(view -> validarDados());
    }

    private void recuperarEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(FirebaseHelper.getIdFirebase());
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        Picasso.get().load(empresa.getUrlLogo()).into(imgLogo);
        edtNome.setText(empresa.getNome());
        edtTelefone.setText(empresa.getTelefone());
        edtTaxaEntrega.setText(String.valueOf(empresa.getTaxaEntrega()));
        edtPedidoMinimo.setText(String.valueOf(empresa.getPedidoMinimo()));
        edtTempoMinimo.setText(String.valueOf(empresa.getTempoMinEntrega()));
        edtTempoMaximo.setText(String.valueOf(empresa.getTempoMaxEntrega()));
        edtCategoria.setText(empresa.getCategoria());

        configSalvar(false);
    }

    private void configSalvar(boolean showProgressBar) {
        if (showProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
            ibSalvar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            ibSalvar.setVisibility(View.VISIBLE);
        }

    }

    public void selecionarLogo(View view) {
        verificarPermissaoGaleria();
    }

    private void validarDados() {
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

        if (!nome.isEmpty()) {
            if (edtTelefone.isDone()) {
                if (tempoMinimo > 0) {
                    if (tempoMaximo > 0) {
                        if (!categoria.isEmpty()) {

                            ocultarTeclado();

                            configSalvar(true);

                            empresa.setNome(nome);
                            empresa.setTelefone(telefone);
                            empresa.setTaxaEntrega(taxaEntrega);
                            empresa.setPedidoMinimo(pedidoMinimo);
                            empresa.setTempoMinEntrega(tempoMinimo);
                            empresa.setTempoMaxEntrega(tempoMaximo);
                            empresa.setCategoria(categoria);

                            if (caminhoLogo != null) {
                                salvarImagemFirebase();
                            } else {
                                empresa.salvar();
                                configSalvar(false);
                                Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                            }

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

            empresa.setUrlLogo(task.getResult().toString());
            empresa.salvar();

            configSalvar(false);
            Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            configSalvar(false);
            erroSalvarDados(e.getMessage());
        });
    }

    private void verificarPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão negada!", Toast.LENGTH_SHORT).show();
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

    private void erroSalvarDados(String msg) {
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
        textToolbar.setText("Dados da empresa");

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
        ibSalvar = findViewById(R.id.ib_salvar);
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