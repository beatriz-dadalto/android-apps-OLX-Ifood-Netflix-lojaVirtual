package com.beatriz.olx_clone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beatriz.olx_clone.R;
import com.beatriz.olx_clone.api.CEPService;
import com.beatriz.olx_clone.helper.FirebaseHelper;
import com.beatriz.olx_clone.helper.GetMask;
import com.beatriz.olx_clone.model.Anuncio;
import com.beatriz.olx_clone.model.Categoria;
import com.beatriz.olx_clone.model.Endereco;
import com.beatriz.olx_clone.model.ImagemUpload;
import com.beatriz.olx_clone.model.Local;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormAnuncioActivity extends AppCompatActivity {
    private final int REQUEST_CATEGORIA = 100;

    private ImageView imagem0;
    private ImageView imagem1;
    private ImageView imagem2;

    private Button btn_categoria;
    private EditText edt_titulo;
    private EditText edt_descricao;
    private CurrencyEditText edt_valor;
    private MaskEditText edt_cep;
    private ProgressBar progressBar;
    private TextView txt_local;
    private TextView text_toolbar;
    private Button btn_salvar;

    private String categoriaSelecionada = "";

    private Endereco enderecoUsuario;
    private Local local;

    private Retrofit retrofit;
    private String currentPhotoPath;

    private final List<ImagemUpload> imagemUploadList = new ArrayList<>();
    private Anuncio anuncio;
    private boolean novoAnuncio = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaCompnentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            anuncio = (Anuncio) bundle.getSerializable("anuncioSelecionado");

            configDados();
        }

        iniciaRetrofit();

        recuperaEndereco();

        configCliques();

    }

    private void configDados() {
        text_toolbar.setText("Editando anúncio");

        categoriaSelecionada = anuncio.getCategoria();
        btn_categoria.setText(categoriaSelecionada);

        edt_titulo.setText(anuncio.getTitulo());
        edt_valor.setText(GetMask.getValor(anuncio.getValor()));
        edt_descricao.setText(anuncio.getDescricao());

        Picasso.get().load(anuncio.getUrlImagens().get(0).getCaminhoImagem()).into(imagem0);
        Picasso.get().load(anuncio.getUrlImagens().get(1).getCaminhoImagem()).into(imagem1);
        Picasso.get().load(anuncio.getUrlImagens().get(2).getCaminhoImagem()).into(imagem2);

        novoAnuncio = false;
    }

    public void validarDados(View view) {
        String titulo = edt_titulo.getText().toString();
        double valor = (double) edt_valor.getRawValue() / 100;
        String descricao = edt_descricao.getText().toString();

        if (!titulo.isEmpty()) {
            if (valor > 0) {
                if (!categoriaSelecionada.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        if (local != null) {
                            if (local.getLocalidade() != null) {

                                if (anuncio == null) anuncio = new Anuncio();
                                anuncio.setIdUsuario(FirebaseHelper.getIdFirebase());
                                anuncio.setTitulo(titulo);
                                anuncio.setValor(valor);
                                anuncio.setCategoria(categoriaSelecionada);
                                anuncio.setDescricao(descricao);
                                anuncio.setLocal(local);

                                if(novoAnuncio){ // Novo Anúncio
                                    if(imagemUploadList.size() == 3){
                                        for (int i = 0; i < imagemUploadList.size(); i++) {
                                            salvarImagemFirebase(imagemUploadList.get(i), i);
                                        }
                                    }else {
                                        Toast.makeText(this, "Selecione 3 imagens para o anúncio.", Toast.LENGTH_SHORT).show();
                                    }
                                }else { // Edição
                                    if(imagemUploadList.size() > 0){
                                        for (int i = 0; i < imagemUploadList.size(); i++) {
                                            salvarImagemFirebase(imagemUploadList.get(i), i);
                                        }
                                    }else {
                                        btn_salvar.setText("Salvando...");
                                        anuncio.salvar(this, false);
                                    }
                                }

                            } else {
                                Toast.makeText(this, "Digite um CEP válido.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Digite um CEP válido.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edt_descricao.requestFocus();
                        edt_descricao.setError("Informe a descrição.");
                    }
                } else {
                    Toast.makeText(this, "Selecione uma categoria.", Toast.LENGTH_SHORT).show();
                }
            } else {
                edt_valor.requestFocus();
                edt_valor.setError("Informe um valor válido.");
            }
        } else {
            edt_titulo.requestFocus();
            edt_titulo.setError("Informe o título.");
        }

    }

    private void salvarImagemFirebase(ImagemUpload imagemUpload, int index){

        btn_salvar.setText("Salvando...");

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                .child(anuncio.getId())
                .child("imagem" + index + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(imagemUpload.getCaminhoImagem()));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            imagemUpload.setCaminhoImagem(task.getResult().toString());

            if(novoAnuncio){
                anuncio.getUrlImagens().add(imagemUpload);
            }else {
                anuncio.getUrlImagens().set(index, imagemUpload);
            }

            if(imagemUploadList.size() == index + 1){
                anuncio.salvar(this, novoAnuncio);
            }

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        imagem0.setOnClickListener(v -> showBottomDialog(0));
        imagem1.setOnClickListener(v -> showBottomDialog(1));
        imagem2.setOnClickListener(v -> showBottomDialog(2));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int requestCode) {
        int request = 0;
        switch (requestCode) {
            case 0:
                request = 3;
                break;
            case 1:
                request = 4;
                break;
            case 2:
                request = 5;
                break;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.apsmobile.olxclone.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, request);
        }
    }

    public void selecionarCategoria(View view) {
        Intent intent = new Intent(this, CategoriasActivity.class);
        startActivityForResult(intent, REQUEST_CATEGORIA);
    }

    private void recuperaEndereco() {
        configCep();

        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    enderecoUsuario = snapshot.getValue(Endereco.class);
                    if (enderecoUsuario != null) edt_cep.setText(enderecoUsuario.getCep());
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getBaseContext(), "Precisamos do seu endereço.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), EnderecoActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomDialog(int requestCode) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(modalbottomsheet);
        bottomSheetDialog.show();

        modalbottomsheet.findViewById(R.id.btn_camera).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            verificaPermissaoCamera(requestCode);
        });

        modalbottomsheet.findViewById(R.id.btn_galeria).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            verificaPermissaoGaleria(requestCode);
        });

        modalbottomsheet.findViewById(R.id.btn_close).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Fechando", Toast.LENGTH_SHORT).show();
        });
    }

    private void verificaPermissaoCamera(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                dispatchTakePictureIntent(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioActivity.this, "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.CAMERA},
                "Se você não aceitar a permissão não poderá acessar a Câmera do dispositivo, deseja ativar a permissão agora ?");
    }

    private void configUpload(int requestCode, String caminhoImagem) {
        int request = 0;
        switch (requestCode) {
            case 0:
            case 3:
                request = 0;
                break;
            case 1:
            case 4:
                request = 1;
                break;
            case 2:
            case 5:
                request = 2;
                break;
        }

        ImagemUpload imagemUpload = new ImagemUpload(caminhoImagem, request);
        if(imagemUploadList.size() > 0){
            boolean encontrou = false;
            for (int i = 0; i < imagemUploadList.size(); i++) {
                if(imagemUploadList.get(i).getIndex() == request){
                    encontrou = true;
                }
            }

            if(encontrou){
                imagemUploadList.set(request, imagemUpload);
            }else {
                imagemUploadList.add(imagemUpload);
            }
        }else {
            imagemUploadList.add(imagemUpload);
        }
    }

    private void verificaPermissaoGaleria(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioActivity.this, "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?");
    }

    private void abrirGaleria(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void configCep() {
        edt_cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String cep = s.toString().replaceAll("_", "").replace("-", "");

                if (cep.length() == 8) {
                    buscarEndereco(cep);
                } else {

                    local = null;

                    configEndereco();

                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void buscarEndereco(String cep) {
        progressBar.setVisibility(View.VISIBLE);

        CEPService cepService = retrofit.create(CEPService.class);
        Call<Local> call = cepService.recuperarCEP(cep);

        call.enqueue(new Callback<Local>() {
            @Override
            public void onResponse(Call<Local> call, Response<Local> response) {
                if (response.isSuccessful()) {

                    local = response.body();

                    if (local.getLocalidade() == null) {
                        Toast.makeText(FormAnuncioActivity.this, "CEP inválido.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(FormAnuncioActivity.this, "Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                }

                configEndereco();
            }

            @Override
            public void onFailure(Call<Local> call, Throwable t) {
                Toast.makeText(FormAnuncioActivity.this, "Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configEndereco() {
        if (local != null) {
            if (local.getLocalidade() != null) {

                String endereco = local.getLocalidade() + ", " + local.getBairro() + " - DDD " + local.getDdd();
                txt_local.setText(endereco);

            } else {
                txt_local.setText("");
            }
        } else {
            txt_local.setText("");
        }
        progressBar.setVisibility(View.GONE);
    }

    // Então esse é um dos possível erros que a gente pode ter

    private void iniciaRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void iniciaCompnentes() {
        text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo anúncio");

        imagem0 = findViewById(R.id.imagem0);
        imagem1 = findViewById(R.id.imagem1);
        imagem2 = findViewById(R.id.imagem2);

        edt_titulo = findViewById(R.id.edt_titulo);
        edt_descricao = findViewById(R.id.edt_descricao);

        edt_valor = findViewById(R.id.edt_valor);
        edt_valor.setLocale(new Locale("PT", "br"));
        btn_categoria = findViewById(R.id.btn_categoria);
        edt_cep = findViewById(R.id.edt_cep);
        progressBar = findViewById(R.id.progressBar);
        txt_local = findViewById(R.id.txt_local);
        btn_salvar = findViewById(R.id.btn_salvar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap bitmap0;
            Bitmap bitmap1;
            Bitmap bitmap2;

            Uri imagemSelecionada = data.getData();
            String caminhaImagem;

            if (requestCode == REQUEST_CATEGORIA) {
                Categoria categoria = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                categoriaSelecionada = categoria.getNome();
                btn_categoria.setText(categoriaSelecionada);
            } else if (requestCode <= 2) { // Galeria

                try {
                    caminhaImagem = imagemSelecionada.toString();

                    switch (requestCode) {
                        case 0:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap0 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap0 = ImageDecoder.decodeBitmap(source);
                            }
                            imagem0.setImageBitmap(bitmap0);
                            break;
                        case 1:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap1 = ImageDecoder.decodeBitmap(source);
                            }
                            imagem1.setImageBitmap(bitmap1);
                            break;
                        case 2:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap2 = ImageDecoder.decodeBitmap(source);
                            }
                            imagem2.setImageBitmap(bitmap2);
                            break;
                    }

                    configUpload(requestCode, caminhaImagem);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else { // Camera
                File file = new File(currentPhotoPath);

                caminhaImagem = String.valueOf(file.toURI());

                switch (requestCode) {
                    case 3:
                        imagem0.setImageURI(Uri.fromFile(file));
                        break;
                    case 4:
                        imagem1.setImageURI(Uri.fromFile(file));
                        break;
                    case 5:
                        imagem2.setImageURI(Uri.fromFile(file));
                        break;
                }

                configUpload(requestCode, caminhaImagem);

            }
        }

    }
}