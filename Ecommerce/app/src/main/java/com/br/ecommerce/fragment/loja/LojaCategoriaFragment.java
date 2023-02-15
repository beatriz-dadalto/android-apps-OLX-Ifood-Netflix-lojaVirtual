package com.br.ecommerce.fragment.loja;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.adapter.CategoriaAdapter;
import com.br.ecommerce.databinding.DialogDeleteBinding;
import com.br.ecommerce.databinding.DialogFormCategoriaBinding;
import com.br.ecommerce.databinding.FragmentLojaCategoriaBinding;
import com.br.ecommerce.helper.FirebaseHelper;
import com.br.ecommerce.model.Categoria;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LojaCategoriaFragment extends Fragment implements CategoriaAdapter.OnClick {

    private DialogFormCategoriaBinding categoriaBinding;

    private FragmentLojaCategoriaBinding binding;
    private AlertDialog dialog;

    private String caminhoImagem = null;

    private Categoria categoria;

    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLojaCategoriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recuperaCategorias();
        configCliques();
        configRv();
    }

    private void configRv() {
        binding.rvCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(R.layout.item_categoria_vertical, false, categoriaList, this, requireContext());
        binding.rvCategorias.setAdapter(categoriaAdapter);

        binding.rvCategorias.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(categoriaList.get(position));
            }
        });
    }

    private void recuperaCategorias() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias");
        categoriaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categoriaList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Categoria categoria = ds.getValue(Categoria.class);
                        categoriaList.add(categoria);
                    }
                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhuma categoria cadastrada.");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(categoriaList);
                categoriaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques() {
        binding.btnAddCategoria.setOnClickListener(view -> {
            categoria = null;
            showDialog();
        });
    }

    private void showDialogDelete(Categoria categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                R.style.CustomAlertDialog);

        DialogDeleteBinding deleteBinding = DialogDeleteBinding.
                inflate(LayoutInflater.from(getContext()));

        deleteBinding.btnFechar.setOnClickListener(view -> {
            dialog.dismiss();
            categoriaAdapter.notifyDataSetChanged();
        });

        deleteBinding.textTitulo.setText("Deseja remover essa categoria?");

        deleteBinding.btnSim.setOnClickListener(view -> {
            categoriaList.remove(categoria); // lista local

            if (categoriaList.isEmpty()) {
                binding.textInfo.setText("Nenhuma categoria cadastrada.");
            } else {
                binding.textInfo.setText("");
            }

            categoria.delete(); // firebase

            categoriaAdapter.notifyDataSetChanged();

            dialog.dismiss();
        });

        builder.setView(deleteBinding.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                R.style.CustomAlertDialog);

        categoriaBinding = DialogFormCategoriaBinding.
                inflate(LayoutInflater.from(getContext()));

        if (categoria != null) {
            categoriaBinding.edtCategoria.setText(categoria.getNome());
            Glide
                    .with(this)
                    .load(categoria.getUrlImagem())
                    .centerCrop()
                    .into(categoriaBinding.imagemCategoria);
            categoriaBinding.cbTodosProdutos.setChecked(categoria.isTodas());
        }

        categoriaBinding.btnFechar.setOnClickListener(view -> dialog.dismiss());

        categoriaBinding.btnSalvar.setOnClickListener(view -> {
            String nomeCategoria = categoriaBinding.edtCategoria.getText().toString().trim();

            if (!nomeCategoria.isEmpty()) {

                if (categoria == null) categoria = new Categoria();
                categoria.setNome(nomeCategoria);
                categoria.setTodas(categoriaBinding.cbTodosProdutos.isChecked());

                ocultarTeclado();
                categoriaBinding.progressBar.setVisibility(View.VISIBLE);

                if (caminhoImagem != null) { // novo cadastro ou edicao da imagem
                    ocultarTeclado();
                    salvarImagemFirebase();
                } else if (categoria.getUrlImagem() != null) { // edicao de nome ou checkbox
                    categoria.salvar();
                    dialog.dismiss();
                } else { // nao preencheu a imagem
                    categoriaBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Escolha uma imagem para categoria.", Toast.LENGTH_SHORT).show();
                }
            } else {
                categoriaBinding.edtCategoria.setError("Informação obrigatória.");
            }
        });

        categoriaBinding.imagemCategoria.setOnClickListener(view -> verificaPermissaoGaleria());

        builder.setView(categoriaBinding.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("categorias")
                .child(categoria.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            String urlImagem = task.getResult().toString();
            categoria.setUrlImagem(urlImagem);
            categoria.salvar();

            categoria = null;
            dialog.dismiss();

        })).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
        });
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permissão negada\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a galeria do dispositivo. Deseja aceitar agora?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void abrirGaleria() {
        // acessar a pasta de midias da galeria
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {

                    // recuperar caminho da imagem
                    Uri imagemSelecionada = result.getData().getData();
                    caminhoImagem = imagemSelecionada.toString();

                    Bitmap bitmap;

                    try {

                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagemSelecionada);
                        } else {
                            ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imagemSelecionada);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }

                        categoriaBinding.imagemCategoria.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
    );

    private void ocultarTeclado() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClickListener(Categoria categoria) {
        this.categoria = categoria;
        showDialog();
    }
}