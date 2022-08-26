package com.beatriz.olx_clone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.model.Usuario;
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

public class PerfilActivity extends AppCompatActivity {

    private final int SELECAO_GALERIA = 100;

    private EditText edtNome;
    private MaskEditText edtTelefone;
    private EditText edtEmail;
    private ProgressBar progressBar;
    private ImageView imagemPerfil;
    private String caminhoImagem;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iniciarComponentes();

        configCliques();

        recuperarPerfil();
    }

    private void recuperarPerfil() {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference perfilRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        perfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void salvarImagemPerfil() {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("perfil")
                .child(FirebaseHelper.getIdFirebase() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            String urlImagem = task.getResult().toString();
            usuario.setImagemPerfil(urlImagem);
            usuario.salvar(progressBar, getBaseContext());

        })).addOnFailureListener(e -> Toast.makeText(this, "Erro no upload, tente novamente mais tarde.", Toast.LENGTH_SHORT).show());
    }

    private void configDados() {
        edtNome.setText(usuario.getNome());
        edtTelefone.setText(usuario.getTelefone());
        edtEmail.setText(usuario.getEmail());

        progressBar.setVisibility(View.GONE);

        if (usuario.getImagemPerfil() != null) {
            Picasso.get().load(usuario.getImagemPerfil()).into(imagemPerfil);
        }
    }

    public void validarDados(View view) {
        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getMasked();

        if (!nome.isEmpty()) {
            if (!telefone.isEmpty()) {
                if (telefone.length() == 15) {

                    progressBar.setVisibility(View.VISIBLE);

                    if (caminhoImagem != null) {
                        salvarImagemPerfil();
                    } else {
                        // TODO update - salvar as modificacoes no firebase
                        // quando modifica os dados do user nao esta atualizando no firebase
                        usuario.salvar(progressBar, getBaseContext());
                    }
                } else {
                    edtTelefone.requestFocus();
                    edtTelefone.setError("Telefone inválido.");
                }
            } else {
                edtTelefone.requestFocus();
                edtTelefone.setError("Preencha o telefone.");
            }
        } else {
            edtNome.requestFocus();
            edtNome.setError("Preencha o nome.");
        }
    }

    private void configCliques() {
        imagemPerfil.setOnClickListener(v -> verificarPermissaoGaleria());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void verificarPermissaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PerfilActivity.this, "Permissões Negadas.", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissões negadas")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        int SELECAO_GALERIA = 100;
        startActivityForResult(intent, SELECAO_GALERIA);
    }

    private void iniciarComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Perfil");

        edtNome = findViewById(R.id.edt_nome);
        edtTelefone = findViewById(R.id.edt_telefone);
        edtEmail = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);

        imagemPerfil = findViewById(R.id.imagem_perfil);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Uri imagemSelecionada = data.getData();
            Bitmap imagemRecuperada;

            try {
                imagemRecuperada = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                imagemPerfil.setImageBitmap(imagemRecuperada);

                caminhoImagem = imagemSelecionada.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}