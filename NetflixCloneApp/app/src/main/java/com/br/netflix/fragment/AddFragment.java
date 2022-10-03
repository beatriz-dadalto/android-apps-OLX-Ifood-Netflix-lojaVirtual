package com.br.netflix.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.netflix.R;
import com.br.netflix.autenticacao.LoginActivity;
import com.br.netflix.helper.FirebaseHelper;
import com.br.netflix.model.Post;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class AddFragment extends Fragment {

    private final int SELECAO_GALERIA = 100;

    private Button btnSalvar;
    private ImageView imageView;
    private String caminhoImagem = null;
    private ImageView imageFake;
    private ProgressBar progressBar;

    private EditText editTitulo;
    private EditText editGenero;
    private EditText editElenco;
    private EditText editAno;
    private EditText editDuracao;
    private EditText editSinopse;

    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciaComponentes(view);
        configCliques();

    }

    private void validaDados() {
        if (FirebaseHelper.getAutenticado()) {
            String titulo = editTitulo.getText().toString().trim();
            String genero = editGenero.getText().toString().trim();
            String elenco = editElenco.getText().toString().trim();
            String ano = editAno.getText().toString().trim();
            String duracao = editDuracao.getText().toString().trim();
            String sinopse = editSinopse.getText().toString().trim();

            if (!titulo.isEmpty()) {
                if (!genero.isEmpty()) {
                    if (!elenco.isEmpty()) {
                        if (!ano.isEmpty()) {
                            if (!duracao.isEmpty()) {
                                if (!sinopse.isEmpty()) {
                                    if (caminhoImagem != null) {

                                        ocultarTeclado();
                                        progressBar.setVisibility(View.VISIBLE);

                                        Post post = new Post();
                                        post.setTitulo(titulo);
                                        post.setGenero(genero);
                                        post.setElenco(elenco);
                                        post.setAno(ano);
                                        post.setDuracao(duracao);
                                        post.setSinopse(sinopse);

                                        salvarImagemFirebase(post);

                                    } else {
                                        showDialog("Selecione uma imagem");
                                    }
                                } else {
                                    editSinopse.requestFocus();
                                    editSinopse.setError("Informe a duração em minutos");
                                }
                            } else {
                                editDuracao.requestFocus();
                                editDuracao.setError("Informe a duração em minutos");
                            }
                        } else {
                            editAno.requestFocus();
                            editAno.setError("Informe o ano");
                        }
                    } else {
                        editElenco.requestFocus();
                        editElenco.setError("Informe o elenco");
                    }
                } else {
                    editGenero.requestFocus();
                    editGenero.setError("Informe o gênero");
                }
            } else {
                editTitulo.requestFocus();
                editTitulo.setError("Informe o título");
            }

        } else {
            Toast.makeText(getActivity(), "Entre em sua conta para poder cadastrar!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    // salvar no banco de dados e recuperar a url
    private void salvarImagemFirebase(Post post) {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("posts")
                .child(post.getId() + "jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                .addOnCompleteListener(task -> {

                    post.setImagem(task.getResult().toString());
                    post.salvar();
                    limparCampos();
                    Toast.makeText(getActivity(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

                })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            showDialog("Erro ao cadastrar");
        });
    }

    private void limparCampos() {
        imageView.setImageBitmap(null);
        imageFake.setVisibility(View.VISIBLE);

        editTitulo.getText().clear();
        editGenero.getText().clear();
        editElenco.getText().clear();
        editAno.getText().clear();
        editDuracao.getText().clear();
        editSinopse.getText().clear();
        editTitulo.requestFocus();

        progressBar.setVisibility(View.GONE);
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext(), R.style.CustomAlertDialog
        );

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_info, null);

        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void iniciaComponentes(View view) {
        btnSalvar = view.findViewById(R.id.btnSalvar);
        imageView = view.findViewById(R.id.imageView);
        imageFake = view.findViewById(R.id.imageFake);
        progressBar = view.findViewById(R.id.progressBar);

        editTitulo = view.findViewById(R.id.edtTitulo);
        editGenero = view.findViewById(R.id.edtGenero);
        editElenco = view.findViewById(R.id.edtElenco);
        editAno = view.findViewById(R.id.edtAno);
        editDuracao = view.findViewById(R.id.edtDuracao);
        editSinopse = view.findViewById(R.id.edtSinopse);
    }

    private void configCliques() {
        imageView.setOnClickListener(v -> verificaPermissaoGaleria());
        btnSalvar.setOnClickListener(v -> validaDados());
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permissão negada.", Toast.LENGTH_LONG).show();
            }
        };

        // ALERT DIALOG
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a galeria " +
                        "do celular. Deseja permitir o acesso?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECAO_GALERIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECAO_GALERIA) {
                // pegar a imagem em URI e transforma-la em string e depois transforma-la em
                // bitmap para setar no componente da tela
                Uri imagemSelecionada = data.getData();
                caminhoImagem = imagemSelecionada.toString();

                Bitmap bitmap;
                try {

                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagemSelecionada);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imagemSelecionada);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }

                    imageFake.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void ocultarTeclado() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}