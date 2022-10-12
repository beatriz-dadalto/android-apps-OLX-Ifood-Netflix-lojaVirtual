package com.br.ecommerce.activity.loja;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.br.ecommerce.R;
import com.br.ecommerce.databinding.ActivityLojaFormProdutoBinding;
import com.br.ecommerce.databinding.BottomSheetFormProdutoBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class LojaFormProdutoActivity extends AppCompatActivity {

    private ActivityLojaFormProdutoBinding binding;

    private int codeImagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaFormProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.imagemProduto0.setOnClickListener(view -> showBottomSheet(0));
        binding.imagemProduto1.setOnClickListener(view -> showBottomSheet(1));
        binding.imagemProduto2.setOnClickListener(view -> showBottomSheet(2));
    }

    private void showBottomSheet(int codeImagePosition) {

        this.codeImagePosition = codeImagePosition;

        BottomSheetFormProdutoBinding bottomSheetBinding =
                BottomSheetFormProdutoBinding.inflate(LayoutInflater.from(this));

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(this, R.style.BottomSheetDialog);

        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();

        bottomSheetBinding.btnCamera.setOnClickListener(view -> {
            verificaPermissaoCamera();
            bottomSheetDialog.dismiss();
        });

        bottomSheetBinding.btnGaleria.setOnClickListener(view -> {
            verificaPermissaoGaleria();
            bottomSheetDialog.dismiss();
        });

        bottomSheetBinding.btnCancelar.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });
    }

    private void verificaPermissaoCamera() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirCamera();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.CAMERA},
                "Se você não aceitar a permissão não poderá acessar a câmera. Deseja aceitar a permissão agora?");
    }

    private void abrirCamera() {
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Se você não aceitar a permissão não poderá acessar a galeria. Deseja aceitar a permissão agora?");
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
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

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    Uri imagemSelecionada = result.getData().getData();
                    String caminhoImagem;

                    try {
                        caminhoImagem = imagemSelecionada.toString();

                        switch (codeImagePosition) {
                            case 0:
                                binding.imageFake0.setVisibility(View.GONE);
                                binding.imagemProduto0.setImageBitmap(getBitmap(imagemSelecionada));
                                break;
                            case 1:
                                binding.imageFake1.setVisibility(View.GONE);
                                binding.imagemProduto1.setImageBitmap(getBitmap(imagemSelecionada));
                                break;
                            case 2:
                                binding.imageFake2.setVisibility(View.GONE);
                                binding.imagemProduto2.setImageBitmap(getBitmap(imagemSelecionada));
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private Bitmap getBitmap(Uri caminhoUri) {
        Bitmap bitmap = null;

        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), caminhoUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}