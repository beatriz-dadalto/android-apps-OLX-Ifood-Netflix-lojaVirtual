package com.biamailov3.ifoodclone.fragment.usuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biamailov3.ifoodclone.R;
import com.biamailov3.ifoodclone.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class UsuarioPedidoFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_pedido, container, false);


        iniciarComponentes(view);
        configTabsLayout();
        return view;
    }

    private void configTabsLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPagerAdapter.addFragment(new UsuarioPedidoAndamentoFragment(), "Em andamento");
        viewPagerAdapter.addFragment(new UsuarioPedidoFinalizadoFragment(), "Concluídos");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setElevation(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void iniciarComponentes(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
    }
}