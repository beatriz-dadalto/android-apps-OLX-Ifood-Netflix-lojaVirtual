package com.beatriz.casaportemporada.activity;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.casaportemporada.R;
import com.beatriz.casaportemporada.helper.FirebaseHelper;
import com.beatriz.casaportemporada.model.Anuncio;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class FormAnuncioActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;

    private EditText editTitulo;
    private EditText editDescricao;
    private EditText editQuarto;
    private EditText editBanheiro;
    private EditText editGaragem;
    private CheckBox cbStatus;

    private ImageView imgAnuncio;
    private String caminhoImagem;
    private Bitmap imagem; // imagem carregada da galeria
    private ProgressBar progressBar;

    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciarComponentes();

        //recebe os dados de outra activity. aqui esta recebendo de MeusAnunciosActivity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            anuncio = (Anuncio) bundle.getSerializable("anuncio");

            configDados();
        }


        configCliques();
    }

    // recebe os dados pra configurar as infos do anuncio clicado nos componentes
    private void configDados() {
        Picasso.get().load(anuncio.getUrlImagem()).into(imgAnuncio);
        editTitulo.setText(anuncio.getTitulo());
        editDescricao.setText(anuncio.getDescricao());
        editQuarto.setText(anuncio.getQuarto());
        editBanheiro.setText(anuncio.getBanheiro());
        cbStatus.setChecked(anuncio.isStatus());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // verificar se a imagem da galeria é a mesma que foi selecionada
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_GALERIA) {
                Uri localImagemSelecionada = data.getData();
                caminhoImagem = localImagemSelecionada.toString();

                if(Build.VERSION.SDK_INT < 28) {
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

                // imagem da galeria
                imgAnuncio.setImageBitmap(imagem);
            }
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validarDados());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void validarDados() {
        String titulo = editTitulo.getText().toString();
        String descricao = editDescricao.getText().toString();
        String quarto = editQuarto.getText().toString();
        String banheiro = editBanheiro.getText().toString();
        String garagem = editGaragem.getText().toString();

        if(!titulo.isEmpty()) {
            if(!descricao.isEmpty()) {
                if(!quarto.isEmpty()) {
                    if(!banheiro.isEmpty()) {
                        if(!garagem.isEmpty()) {
                            if(anuncio == null) {
                                anuncio = new Anuncio();
                            }
                            anuncio.setIdUsuario(FirebaseHelper.getIdFirebase());
                            anuncio.setTitulo(titulo);
                            anuncio.setDescricao(descricao);
                            anuncio.setQuarto(quarto);
                            anuncio.setBanheiro(banheiro);
                            anuncio.setGaragem(garagem);
                            anuncio.setStatus(cbStatus.isChecked());

                            if(caminhoImagem != null) {
                                salvarImagemAnuncio();
                            } else {
                                if(anuncio.getUrlImagem() != null) {
                                    anuncio.salvar();
                                } else {
                                    Toast.makeText(this, "Selecione uma imagem para o anúncio", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            editGaragem.requestFocus();
                            editGaragem.setError("Informação obrigatória");
                        }
                    } else {
                        editBanheiro.requestFocus();
                        editBanheiro.setError("Informação obrigatória");
                    }
                } else {
                    editQuarto.requestFocus();
                    editQuarto.setError("Informação obrigatória");
                }
            } else {
                editDescricao.requestFocus();
                editDescricao.setError("Digite uma descrição");
            }
        } else {
            editTitulo.requestFocus();
            editTitulo.setError("Digite o título");
        }
    }

    private void salvarImagemAnuncio() {

        progressBar.setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                .child(anuncio.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {
            String urlImagem = task.getResult().toString();
            anuncio.setUrlImagem(urlImagem);

            anuncio.salvar();

            // close the current activity
            finish();
        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void iniciarComponentes() {
        TextView textTitulo = findViewById(R.id.text_titulo);
        textTitulo.setText("Form Anúncio");

        editTitulo = findViewById(R.id.edit_titulo);
        editDescricao = findViewById(R.id.edit_descricao);
        editQuarto = findViewById(R.id.edit_quarto);
        editBanheiro = findViewById(R.id.edit_banheiro);
        editGaragem = findViewById(R.id.edit_garagem);
        cbStatus = findViewById(R.id.cb_status);
        imgAnuncio = findViewById(R.id.img_anuncio);
        progressBar = findViewById(R.id.progressBar);
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    public void verificarPermissaoGaleria(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        
        showDialogPermissaoGaleria(permissionListener, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void showDialogPermissaoGaleria(PermissionListener permissionListener, String[] permissoes) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissões negadas")
                .setDeniedMessage("Você negou as permissões para acessar a galeria do dispositivo. Deseja Permitir agora?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

}