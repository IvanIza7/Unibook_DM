package com.navify.unibook.fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;
import com.navify.unibook.Actividad;
import com.navify.unibook.Materia;

import java.util.List;

public class PerfilFragment extends Fragment {

    private DatabaseHelper db;
    private TextView tvMateriasCount;
    private TextView tvActividadesCount;
    private LinearLayout themeLightSelector;
    private LinearLayout themeDarkSelector;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        db = new DatabaseHelper(getContext());
        tvMateriasCount = view.findViewById(R.id.tvMateriasCount);
        tvActividadesCount = view.findViewById(R.id.tvActividadesCount);
        
        themeLightSelector = view.findViewById(R.id.themeLightSelector);
        themeDarkSelector = view.findViewById(R.id.themeDarkSelector);

        // Configurar listeners para cambio de tema
        themeLightSelector.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        themeDarkSelector.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });

        cargarEstadisticas();

        return view;
    }

    private void cargarEstadisticas() {
        if (db == null) return;
        
        List<Materia> materias = db.obtenerTodasLasMaterias();
        List<Actividad> actividades = db.getTodasLasActividades();

        if (tvMateriasCount != null) {
            tvMateriasCount.setText(String.valueOf(materias.size()));
        }
        
        if (tvActividadesCount != null) {
            tvActividadesCount.setText(String.valueOf(actividades.size()));
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        cargarEstadisticas();
    }
}
