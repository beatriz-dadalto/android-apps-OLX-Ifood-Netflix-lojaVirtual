package com.br.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaFormProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configCliques();
    }

    private void configCliques() {
        binding.imagemProduto0.setOnClickListener(view -> showBottomSheet());
        binding.imagemProduto1.setOnClickListener(view -> showBottomSheet());
        binding.imagemProduto2.setOnClickListener(view -> showBottomSheet());
    }

    private void showBottomSheet() {
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
                new String[] {Manifest.permission.CAMERA},
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
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                "Se você não aceitar a permissão não poderá acessar a galeria. Deseja aceitar a permissão agora?");
    }

    private void abrirGaleria() {
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
}